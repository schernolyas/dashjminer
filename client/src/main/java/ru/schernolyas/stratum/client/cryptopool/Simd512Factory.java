/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.cryptopool;

import fr.cryptohash.SIMD512;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 *
 * @author schernolyas
 */
public class Simd512Factory extends BasePooledObjectFactory<SIMD512> {
    private static final Logger LOG = Logger.getLogger(Simd512Factory.class.getName());
    
    @Override
    public SIMD512 create() throws Exception { 
        return new SIMD512();
    }

    @Override
    public PooledObject<SIMD512> wrap(SIMD512 t) {
         return new DefaultPooledObject<>(t);
    }
    
}
