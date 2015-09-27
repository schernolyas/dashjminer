/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.cryptopool;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import ru.schernolyas.stratum.client.minimg.MimingRecursiveTask;

/**
 * Pool for crypto algorithms
 *
 * @author schernolyas
 */
public class CryptoPool {

    private static final Logger LOG = Logger.getLogger(CryptoPool.class.getName());

    private final static GenericObjectPoolConfig POOL_CONFIG;
    private final static GenericObjectPool<CryptoHolder> CRYPTO_HOLDER_POOL;

    static {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(MimingRecursiveTask.GROUP_SIZE + 3);
        POOL_CONFIG = config;
        
        CRYPTO_HOLDER_POOL = new GenericObjectPool<>(new CryptoHolderFactory(),POOL_CONFIG);

        LOG.log(Level.INFO, "pools created");

    }

    public static GenericObjectPool<CryptoHolder> getCryptoHolderPool() {
        return CRYPTO_HOLDER_POOL;
    }
}
