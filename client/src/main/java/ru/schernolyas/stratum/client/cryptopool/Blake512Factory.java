/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.cryptopool;

import fr.cryptohash.BLAKE512;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 *
 * @author schernolyas
 */
class Blake512Factory extends BasePooledObjectFactory<BLAKE512> {
    private static final Logger LOG = Logger.getLogger(Blake512Factory.class.getName());
    
    @Override
    public BLAKE512 create() throws Exception {        
        LOG.log(Level.INFO, "create object");
        return new BLAKE512();
    }

    @Override
    public PooledObject<BLAKE512> wrap(BLAKE512 t) {
         return new DefaultPooledObject<>(t);
    }

   
    
}
