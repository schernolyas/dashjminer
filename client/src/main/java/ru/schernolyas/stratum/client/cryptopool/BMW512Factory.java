/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.cryptopool;

import fr.cryptohash.BMW512;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 *
 * @author schernolyas
 */
class BMW512Factory extends BasePooledObjectFactory<BMW512> {
    private static final Logger LOG = Logger.getLogger(BMW512Factory.class.getName());
    
    @Override
    public BMW512 create() throws Exception {        
        LOG.log(Level.INFO, "create object");
        return new BMW512();
    }

    @Override
    public PooledObject<BMW512> wrap(BMW512 t) {
         return new DefaultPooledObject<>(t);
    }
    
}
