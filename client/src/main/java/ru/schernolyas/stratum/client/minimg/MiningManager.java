/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.minimg;

import io.vertx.core.eventbus.EventBus;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public MiningManager(EventBus eventBus) {
        super("MiningManagerThread");
        setPriority(3);
        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        LOG.log(Level.INFO, "Thread for MiningManager started");

        while (hasAllDataForMining()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        //  has all data for mining. let's go
        initial = Initial.build(GlobalObjects.getInitialJsonString());
        lastMiningNotify = MiningNotify.build(GlobalObjects.getMiningNotifyJsonStrings().getLast());
        setDifficulty = SetDifficulty.build(GlobalObjects.getSetDifficultyJsonString());
        ClearJobsHolder.setNewValue(lastMiningNotify.isCleanJobs());

        BlockHeaderTemplateProducer blockHeaderTemplateProducer = new BlockHeaderTemplateProducer(lastMiningNotify, initial);
        byte[] blockHeaderTemplate = blockHeaderTemplateProducer.produceBlockHeaderTemplate();
        byte[] currentTarget = DifficultyUtil.calculateTarget(setDifficulty);

        nonceUtil = new NonceTimeUtil(lastMiningNotify.getCurrentTime());
        BlockHeaderTemplateProducer blockHeaderTemplateProducer = new BlockHeaderTemplateProducer(miningNotify, initial);
        byte[] blockHeaderTemplate = blockHeaderTemplateProducer.produceBlockHeaderTemplate();
        startMining(blockHeaderTemplate, currentTarget, nonceUtil);

    }

    private boolean hasAllDataForMining() {
        boolean hasInitial = (GlobalObjects.getInitialJsonString() != null);
        boolean hasMiningNotifyJsonStrings = !(GlobalObjects.getMiningNotifyJsonStrings().isEmpty());
        boolean hasSetDifficultyJsonString = (GlobalObjects.getSetDifficultyJsonString() != null);
        return hasInitial && hasMiningNotifyJsonStrings && hasSetDifficultyJsonString;
    }

    public void startMining(byte[] blockHeaderTemplate, byte[] currentTarget, NonceTimeUtil nonceUtil) {
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
    }

    private void submitShare() {

    }

}
