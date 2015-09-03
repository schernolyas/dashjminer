/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.minimg;

import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Hex;
import ru.schernolyas.stratum.client.utils.NonceTimeUtil;

/**
 *
 * @author schernolyas
 */
public class MiningManager {
    private static final Logger LOG = Logger.getLogger(MiningManager.class.getName());
    

    public MiningManager() {
    }

    public void startMining(byte[] blockHeaderTemplate,byte[] currentTarget,NonceTimeUtil nonceUtil) {
        LOG.log(Level.INFO, "--------------start mining---------------------");
        ForkJoinPool commonForkJoinPool = ForkJoinPool.commonPool();
        //TODO: add read data
        MimingRecursiveTask mimingRecursiveTask = new MimingRecursiveTask(true, blockHeaderTemplate, currentTarget,nonceUtil);
        byte[] resultBlockHeader = commonForkJoinPool.invoke(mimingRecursiveTask);
        LOG.log(Level.INFO, "block header  : {0}", new Object[]{Hex.encodeHexString(resultBlockHeader)});
    }

}
