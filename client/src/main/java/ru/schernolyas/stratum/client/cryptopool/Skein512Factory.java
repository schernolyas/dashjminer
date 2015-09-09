/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.cryptopool;

import fr.cryptohash.Skein512;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 *
 * @author schernolyas
 */
public class Skein512Factory extends BasePooledObjectFactory<Skein512> {
    private static final Logger LOG = Logger.getLogger(Skein512Factory.class.getName());
    
    @Override
    public Skein512 create() throws Exception {        
        LOG.log(Level.INFO, "create object");
        return new Skein512();
    }

    @Override
    public PooledObject<Skein512> wrap(Skein512 t) {
         return new DefaultPooledObject<>(t);
    }
    
}
