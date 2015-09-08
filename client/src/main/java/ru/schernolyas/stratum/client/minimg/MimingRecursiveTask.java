/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.minimg;

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

    private static final int GROUP_SIZE = Runtime.getRuntime().availableProcessors() * 2;
    //private static final int GROUP_SIZE =1;
    private boolean isManagerTask = true;
    private byte[] blockHeaderTemplate;
    private NonceTimeHolder nonceTimeHolder;
    private byte[] currentTarget;
    private BigInteger currentTargetInt;
    private byte[] result;
    private BlockHeaderCandidateProducer blockHeaderCandidateProducer;
    private int testCount = 0;

    public MimingRecursiveTask(boolean isManagerTask, byte[] blockHeaderTemplate, byte[] currentTarget,NonceTimeHolder nonceTimeHolder) {
        this.isManagerTask = isManagerTask;
        this.blockHeaderTemplate = blockHeaderTemplate;
        this.currentTarget = currentTarget;
        this.nonceTimeHolder = nonceTimeHolder;
        if (!this.isManagerTask) {
            this.blockHeaderCandidateProducer = new BlockHeaderCandidateProducer(blockHeaderTemplate, nonceTimeHolder);
            this.currentTargetInt = ByteUtils.toBigInteger(currentTarget);
        }
    }

    @Override
    protected byte[] compute() {
        return isManagerTask ? managerCompute() : workerCompute();
    }

    private byte[] managerCompute() {
        long maxIteractions = NonceTimeHolderImpl.MAX_NONCE;
        
       // do {
        for(int i=0;i<100000;i++) {
            LOG.log(Level.INFO, "iteration  : {0}", new Object[]{i});
             List<MimingRecursiveTask> forks = createSubtasks();
            for (Iterator<MimingRecursiveTask> iterator = forks.iterator(); iterator.hasNext();) {
                MimingRecursiveTask subtask = iterator.next();
                subtask.fork();
            }

            for (MimingRecursiveTask subtask : forks) {
                byte[] localResult = subtask.join();
                if (localResult != null) {
                    result = localResult;
                }
            }
        }
        //} while (defineNeedRunNextIteration(result));
        return result;

    }

    private boolean defineNeedRunNextIteration(byte[] result) {
        boolean needRunNextIteration = false;
        /*if (testCount==0) {
            needRunNextIteration = true;
            testCount++;
        } else {
            needRunNextIteration = false;
        } */
        return needRunNextIteration;
    }
    
    private boolean __defineNeedRunNextIteration(byte[] result) {
        boolean needRunNextIteration = false;
        boolean needClearJobs = ClearJobsHolder.needClearJobs();
        boolean hasNewBlock = (result != null);
        if (needClearJobs) {
            needRunNextIteration = false;
        } else {
            needRunNextIteration = !hasNewBlock;
        }
        return needRunNextIteration;
    }

    private List<MimingRecursiveTask> createSubtasks() {
        List<MimingRecursiveTask> subtasks = new ArrayList<>(GROUP_SIZE);
        for (int i = 0; i < GROUP_SIZE; i++) {
            MimingRecursiveTask subtask = new MimingRecursiveTask(false, blockHeaderTemplate, currentTarget,nonceTimeHolder);
            subtasks.add(subtask);
        }
        return subtasks;
    }

    private byte[] workerCompute() {
        byte[] blockHeaderCandidate = blockHeaderCandidateProducer.produceBlockHeaderCandidate();
        LOG.log(Level.INFO, "block header candidate  : {0}", new Object[]{Hex.encodeHexString(blockHeaderCandidate)});
        byte[] x11Hash = X11Util.calculate(blockHeaderCandidate);
        byte[] littleEndianX11Hash = ByteUtils.littleEndian(x11Hash);
        LOG.log(Level.INFO, "littleEndianX11Hash  : {0}", new Object[]{Hex.encodeHexString(littleEndianX11Hash)});
        //TODO: try to compare bytes to first value which not equals
        BigInteger littleEndianX11HashInt = ByteUtils.toBigInteger(littleEndianX11Hash);
        byte[] result = null;
        if (currentTargetInt.compareTo(littleEndianX11HashInt) == 1) {
            result = blockHeaderCandidate;
        }
        return result;
    }

}
