/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.minimg;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.io.IOException;
import java.math.BigInteger;
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
import ru.schernolyas.stratum.client.net.Consumers;
import ru.schernolyas.stratum.client.utils.ByteUtils;
import ru.schernolyas.stratum.client.utils.DifficultyUtil;
import ru.schernolyas.stratum.client.utils.NonceTimeHolder;
import ru.schernolyas.stratum.client.utils.NonceTimeHolderImpl;

/**
 *
 * @author schernolyas
 */
public class MiningWorkerVerticle extends AbstractVerticle {

    private static final Logger LOG = Logger.getLogger(MiningWorkerVerticle.class.getName());

    private MimingRecursiveTask mimingRecursiveTask;
    private ForkJoinPool miningForkJoinPool;

    @Override
    public void start() throws Exception {
        EventBus eventBus = getVertx().eventBus();
        eventBus.consumer(Consumers.NOTIFY, new NotifyHandler());
        eventBus.consumer(Consumers.INITIAL, new Handler<Message<String>>() {

            @Override
            public void handle(Message<String> message) {
                LOG.log(Level.INFO, "get initial message : {0}", new Object[]{message.body()});
                GlobalObjects.setInitial(message.body());
            }
        });
        eventBus.consumer(Consumers.SET_DIFFICULTY, new Handler<Message<String>>() {

            @Override
            public void handle(Message<String> message) {
                LOG.log(Level.INFO, "get message of type 'mining.set_difficulty': {0}", new Object[]{message.body()});
                GlobalObjects.setSetDifficulty(message.body());
            }
        });
    }

    private class NotifyHandler implements Handler<Message<String>> {

        private BigInteger currectJobId;
        private BlockHeaderTemplateProducer blockHeaderTemplateProducer;

        public BigInteger getCurrectJobId() {
            return currectJobId;
        }

        public void setCurrectJobId(BigInteger currectJobId) {
            this.currectJobId = currectJobId;
        }

        public BlockHeaderTemplateProducer getBlockHeaderTemplateProducer() {
            return blockHeaderTemplateProducer;
        }

        public void setBlockHeaderTemplateProducer(BlockHeaderTemplateProducer blockHeaderTemplateProducer) {
            this.blockHeaderTemplateProducer = blockHeaderTemplateProducer;
        }

        @Override
        public void handle(Message<String> message) {

            LOG.log(Level.INFO, "get message of type 'mining.notify': {0}", new Object[]{message.body()});
            LOG.log(Level.INFO, "[Main] Running in  {0}", new Object[]{Thread.currentThread().getName()});
            try {
                MiningNotify notify = MiningNotify.build(message.body());
                BigInteger jobId = ByteUtils.toBigInteger(notify.getJobId());
                Initial initial = Initial.build(GlobalObjects.getInitialJsonString());
                //SetDifficulty setDifficulty = SetDifficulty.build(GlobalObjects.getSetDifficultyJsonString());
                NonceTimeHolder nonceHolder = new NonceTimeHolderImpl();
                byte[] currentTarget = DifficultyUtil.calculateTarget(notify.getEncodedNetworkDifficulty());
                boolean cleanJobs = notify.isCleanJobs();
                if (cleanJobs) {
                    LOG.log(Level.SEVERE, "---CLEAN JOBS-----");
                    if (MiningWorkerVerticle.this.mimingRecursiveTask != null) {
                        MiningWorkerVerticle.this.mimingRecursiveTask.cancel(true);
                    }
                }

                if (getCurrectJobId() == null) {
                    setBlockHeaderTemplateProducer(new BlockHeaderTemplateProducer(notify, initial));
                    startMining(
                            getBlockHeaderTemplateProducer().produceBlockHeaderTemplate(),
                            currentTarget, nonceHolder);
                } else if (getCurrectJobId().compareTo(jobId) == 0) {
                    //the work in progress.  update data in work
                    setBlockHeaderTemplateProducer(new BlockHeaderTemplateProducer(notify, initial));
                }

            } catch (NoSuchAlgorithmException | IOException | DecoderException ex) {
                Logger.getLogger(MiningWorkerVerticle.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        /**
         * start mining process
         *
         * @param blockHeaderTemplate
         * @param currentTarget
         * @param nonceUtil
         * @return true if need get other job
         */
        public boolean startMining(byte[] blockHeaderTemplate, byte[] currentTarget, NonceTimeHolder nonceUtil) {
            LOG.log(Level.INFO, "--------------start mining---------------------");
            //ForkJoinPool commonForkJoinPool = ForkJoinPool.commonPool();
            ForkJoinPool miningForkJoinPool = new ForkJoinPool(MimingRecursiveTask.GROUP_SIZE);
            long start = System.currentTimeMillis();
            MiningWorkerVerticle.this.mimingRecursiveTask = new MimingRecursiveTask(true, nonceUtil);
            MiningWorkerVerticle.this.mimingRecursiveTask.setCurrentTarget(currentTarget);
            MiningWorkerVerticle.this.mimingRecursiveTask.setBlockHeaderTemplate(blockHeaderTemplate);

            byte[] resultBlockHeader = miningForkJoinPool.invoke(MiningWorkerVerticle.this.mimingRecursiveTask);
            long stop = System.currentTimeMillis();
            LOG.log(Level.INFO, "duration: {0} milisec; speed: {1}", new Object[]{stop - start,
                MimingRecursiveTask.GROUP_SIZE * 100000 / ((stop - start) / 1000)});
            LOG.log(Level.INFO, "block header  : {0}",
                    new Object[]{resultBlockHeader != null ? Hex.encodeHexString(resultBlockHeader) : "null"});

            miningForkJoinPool.shutdownNow();
            return true;
        }
    }

}
