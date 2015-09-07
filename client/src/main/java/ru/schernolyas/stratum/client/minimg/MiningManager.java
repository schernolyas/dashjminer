/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.minimg;

import io.vertx.core.eventbus.EventBus;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import ru.schernolyas.stratum.client.blockheader.BlockHeaderTemplateProducer;
import ru.schernolyas.stratum.client.method.Initial;
import ru.schernolyas.stratum.client.method.MiningNotify;
import ru.schernolyas.stratum.client.method.SetDifficulty;
import ru.schernolyas.stratum.client.utils.ClearJobsHolder;
import ru.schernolyas.stratum.client.utils.DifficultyUtil;
import ru.schernolyas.stratum.client.utils.NonceTimeUtil;

/**
 *
 * @author schernolyas
 */
public class MiningManager extends Thread {

    private static final Logger LOG = Logger.getLogger(MiningManager.class.getName());
    private EventBus eventBus;
    private Initial initial = null;
    private MiningNotify lastMiningNotify = null;
    private SetDifficulty setDifficulty = null;
    private NonceTimeUtil nonceUtil = null;
    private BlockHeaderTemplateProducer blockHeaderTemplateProducer;

    public MiningManager() {
        super("MiningManagerThread");
        setPriority(3);
    }

    public MiningManager(EventBus eventBus) {
        this();
        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        LOG.log(Level.INFO, "Thread for MiningManager started");
        LOG.log(Level.INFO, "has all data for start mining? {0}", new Object[]{hasAllDataForMining()});

        while (!hasAllDataForMining()) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        nonceUtil = new NonceTimeUtil();
        //while (true) {
        for (int i = 0; i < 1; i++) {
            try {
                //  has all data for mining. let's go
                LOG.log(Level.INFO, "GlobalObjects.getInitialJsonString(): {0}", new Object[]{GlobalObjects.getInitialJsonString()});
                initial = Initial.build(GlobalObjects.getInitialJsonString());
                LOG.log(Level.INFO, "GlobalObjects.getLastMiningNotifyJsonString(): {0}", new Object[]{GlobalObjects.getLastMiningNotifyJsonString()});
                lastMiningNotify = MiningNotify.build(GlobalObjects.getLastMiningNotifyJsonString());
                LOG.log(Level.INFO, "GlobalObjects.getSetDifficultyJsonString(): {0}", new Object[]{GlobalObjects.getSetDifficultyJsonString()});
                setDifficulty = SetDifficulty.build(GlobalObjects.getSetDifficultyJsonString());
                ClearJobsHolder.setNewValue(lastMiningNotify.isCleanJobs());

                blockHeaderTemplateProducer = new BlockHeaderTemplateProducer(lastMiningNotify, initial);
                byte[] blockHeaderTemplate = blockHeaderTemplateProducer.produceBlockHeaderTemplate();
                byte[] currentTarget = DifficultyUtil.calculateTarget(setDifficulty);
                LOG.log(Level.INFO, "currentTarget: {0}", new Object[]{Hex.encodeHexString(currentTarget)});

                startMining(blockHeaderTemplate, currentTarget, nonceUtil);
                if (ClearJobsHolder.needClearJobs()) {
                    ClearJobsHolder.setNewValue(false);
                }
            } catch (DecoderException | IOException e) {
                //incorrect data
                LOG.log(Level.SEVERE, "Error", e);
            } catch (NoSuchAlgorithmException e1) {
                throw new RuntimeException(e1);
            }
        }
    }

    private boolean hasAllDataForMining() {
        LOG.log(Level.INFO, "check all data");
        boolean hasInitial = (GlobalObjects.getInitialJsonString() != null);
        LOG.log(Level.INFO, "hasInitial: {0}", new Object[]{hasInitial});
        boolean hasMiningNotifyJsonStrings = (GlobalObjects.getLastMiningNotifyJsonString() != null);
        LOG.log(Level.INFO, "hasMiningNotifyJsonStrings: {0}", new Object[]{hasMiningNotifyJsonStrings});
        boolean hasSetDifficultyJsonString = (GlobalObjects.getSetDifficultyJsonString() != null);
        LOG.log(Level.INFO, "hasSetDifficultyJsonString: {0}", new Object[]{hasSetDifficultyJsonString});

        return hasInitial && hasMiningNotifyJsonStrings && hasSetDifficultyJsonString;
    }

    /**
     * start mining process
     *
     * @param blockHeaderTemplate
     * @param currentTarget
     * @param nonceUtil
     * @return true if need get other job
     */
    public boolean startMining(byte[] blockHeaderTemplate, byte[] currentTarget, NonceTimeUtil nonceUtil) {
        LOG.log(Level.INFO, "--------------start mining---------------------");
        ForkJoinPool commonForkJoinPool = ForkJoinPool.commonPool();
        //TODO: add read data
        MimingRecursiveTask mimingRecursiveTask = new MimingRecursiveTask(true, blockHeaderTemplate, currentTarget, nonceUtil);
        byte[] resultBlockHeader = commonForkJoinPool.invoke(mimingRecursiveTask);
        LOG.log(Level.INFO, "block header  : {0}",
                new Object[]{resultBlockHeader != null ? Hex.encodeHexString(resultBlockHeader) : "null"});
        if (resultBlockHeader != null) {
            submitShare();
        }
        return true;
    }

    private void submitShare() {

    }

}
