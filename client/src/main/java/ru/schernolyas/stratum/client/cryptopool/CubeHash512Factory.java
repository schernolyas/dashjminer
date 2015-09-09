/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.cryptopool;

import fr.cryptohash.BMW512;
import fr.cryptohash.CubeHash512;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 *
 * @author schernolyas
 */
public class CubeHash512Factory extends BasePooledObjectFactory<CubeHash512> {
    private static final Logger LOG = Logger.getLogger(CubeHash512Factory.class.getName());
    
    @Override
    public CubeHash512 create() throws Exception {        
        LOG.log(Level.INFO, "create object");
        return new CubeHash512();
    }

    @Override
    public PooledObject<CubeHash512> wrap(CubeHash512 t) {
         return new DefaultPooledObject<>(t);
    }
    
}
