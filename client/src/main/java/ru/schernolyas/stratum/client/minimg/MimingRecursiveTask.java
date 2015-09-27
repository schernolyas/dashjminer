/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.minimg;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Hex;
import ru.schernolyas.stratum.client.blockheader.BlockHeaderCandidateProducer;
import ru.schernolyas.stratum.client.utils.ByteUtils;
import ru.schernolyas.stratum.client.utils.ClearJobsHolder;
import ru.schernolyas.stratum.client.utils.NonceTimeHolder;
import ru.schernolyas.stratum.client.utils.NonceTimeHolderImpl;
import ru.schernolyas.stratum.client.utils.X11Util;

/**
 *
 * @author schernolyas
 */
public class MimingRecursiveTask extends RecursiveTask<byte[]> {

    private static final Logger LOG = Logger.getLogger(MimingRecursiveTask.class.getName());

    public static final int GROUP_SIZE = Runtime.getRuntime().availableProcessors() + 2;
    //private static final int GROUP_SIZE =1;
    private boolean isManagerTask = true;
    private byte[] blockHeaderTemplate;
    private NonceTimeHolder nonceTimeHolder;
    private byte[] currentTarget;
    private byte[] result;
    private BlockHeaderCandidateProducer blockHeaderCandidateProducer;
    private int testCount = 0;
    private boolean stopCalculation;

    public MimingRecursiveTask(boolean isManagerTask, NonceTimeHolder nonceTimeHolder) {
        this.isManagerTask = isManagerTask;
        this.nonceTimeHolder = nonceTimeHolder;
        if (!this.isManagerTask) {
            this.blockHeaderCandidateProducer = new BlockHeaderCandidateProducer(blockHeaderTemplate, nonceTimeHolder);
        }
    }

    public byte[] getBlockHeaderTemplate() {
        return blockHeaderTemplate;
    }

    public void setBlockHeaderTemplate(byte[] blockHeaderTemplate) {
        this.blockHeaderTemplate = blockHeaderTemplate;
        if (!this.isManagerTask) {
            this.blockHeaderCandidateProducer.updateBlockTemplate(blockHeaderTemplate);
        }
    }

    public byte[] getCurrentTarget() {
        return currentTarget;
    }

    public void setCurrentTarget(byte[] currentTarget) {
        this.currentTarget = currentTarget;
    }

    @Override
    protected byte[] compute() {
        byte[] computeResult = null;
        try {
            computeResult = isManagerTask ? managerCompute() : workerCompute();
        } catch (Exception ex) {
            Logger.getLogger(MimingRecursiveTask.class.getName()).log(Level.SEVERE, "Error!", ex);
        }
        return computeResult;
    }

    private byte[] managerCompute() {
        BigInteger maxIteration = NonceTimeHolderImpl.MAX_NONCE.divide(new BigDecimal(GROUP_SIZE).toBigInteger());
        //BigInteger maxIteration = new BigDecimal(100000).toBigInteger().divide(new BigDecimal(GROUP_SIZE).toBigInteger());
        LOG.log(Level.INFO, "maxIteration  : {0}; maxNonce: {1}; GROUP_SIZE: {2}",
                new Object[]{maxIteration, NonceTimeHolderImpl.MAX_NONCE.toString(16), GROUP_SIZE});

        List<MimingRecursiveTask> forks = createSubtasks();
        for (long i = 0; i < maxIteration.longValue(); i++) {
            if ((i % 10000) == 0) {
                LOG.log(Level.INFO, "iteration  : {0}", new Object[]{i});
            }

            for (Iterator<MimingRecursiveTask> iterator = forks.iterator(); iterator.hasNext();) {
                MimingRecursiveTask subtask = iterator.next();
                subtask.fork();
            }

            for (Iterator<MimingRecursiveTask> iterator = forks.iterator(); iterator.hasNext() && !isStopCalculation();) {
                MimingRecursiveTask subtask = iterator.next();
                byte[] localResult = subtask.join();
                if (localResult != null) {
                    result = localResult;
                }
            }

            if (isStopCalculation()) {
                break;
            } else {
                reInit(forks);
            }
        }
        return result;

    }

    private void reInit(List<MimingRecursiveTask> forks) {
        forks.parallelStream().forEach((fork) -> {
            fork.reinitialize();
            fork.setBlockHeaderTemplate(getBlockHeaderTemplate());
            fork.setCurrentTarget(getCurrentTarget());
        });
    }

    private List<MimingRecursiveTask> createSubtasks() {
        List<MimingRecursiveTask> subtasks = new ArrayList<>(GROUP_SIZE);
        for (int i = 0; i < GROUP_SIZE; i++) {
            MimingRecursiveTask subtask = new MimingRecursiveTask(false, nonceTimeHolder);
            subtask.setBlockHeaderTemplate(getBlockHeaderTemplate());
            subtask.setCurrentTarget(getCurrentTarget());
            subtasks.add(subtask);
        }
        return subtasks;
    }

    private byte[] workerCompute() throws Exception {
        byte[] blockHeaderCandidate = blockHeaderCandidateProducer.produceBlockHeaderCandidate();
        byte[] preparedX11BlockHeaderCandidate = ByteUtils.prepareForX11Hash(blockHeaderCandidate);
        byte[] x11Hash = X11Util.calculate(preparedX11BlockHeaderCandidate);
        //LOG.log(Level.INFO, "block header candidate  : {0} ; x11Hash: {1}", new Object[]{Hex.encodeHexString(blockHeaderCandidate),
        //Hex.encodeHexString(x11Hash)});
        byte[] littleEndianX11Hash = ByteUtils.littleEndian(x11Hash);

        byte[] result = null;
        if (ByteUtils.fastCompare(currentTarget, littleEndianX11Hash) == 1) {
            LOG.log(Level.INFO, "littleEndianX11Hash  : {0}; currentTarget: {1}",
                    new Object[]{Hex.encodeHexString(littleEndianX11Hash),
                        Hex.encodeHexString(currentTarget)});
            result = blockHeaderCandidate;
        }
        return result;
    }

    public boolean isStopCalculation() {
        return stopCalculation;
    }

    public void setStopCalculation(boolean stopCalculation) {
        this.stopCalculation = stopCalculation;
    }

}
