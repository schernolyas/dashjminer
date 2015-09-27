/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.cryptopool;

import fr.cryptohash.BMW512;
import java.util.logging.Logger;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 *
 * @author Сергей
 */
public class CryptoHolderFactory extends BasePooledObjectFactory<CryptoHolder> {
    private static final Logger LOG = Logger.getLogger(CryptoHolderFactory.class.getName());
    
    @Override
    public CryptoHolder create() throws Exception {  
        return new CryptoHolder();
    }

    @Override
    public PooledObject<CryptoHolder> wrap(CryptoHolder t) {
         return new DefaultPooledObject<>(t);
    }
    
}
