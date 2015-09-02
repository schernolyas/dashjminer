/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.minimg;

import java.util.concurrent.ForkJoinPool;
import java.util.logging.Logger;

/**
 *
 * @author schernolyas
 */
public class MiningManager {
    private static final Logger LOG = Logger.getLogger(MiningManager.class.getName());
    

    

    public MiningManager() {
    }

    public void startMining() {
        ForkJoinPool commonForkJoinPool = ForkJoinPool.commonPool();
        //TODO: add read data
        MimingRecursiveTask mimingRecursiveTask = new MimingRecursiveTask(true, null, null);
        byte[] resultBlockHeader = commonForkJoinPool.invoke(mimingRecursiveTask);
        

    }

}
