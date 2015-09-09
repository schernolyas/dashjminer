/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.cryptopool;

import fr.cryptohash.JH512;
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
public class JH512Factory extends BasePooledObjectFactory<JH512> {
    private static final Logger LOG = Logger.getLogger(JH512Factory.class.getName());
    
    @Override
    public JH512 create() throws Exception {        
        LOG.log(Level.INFO, "create object");
        return new JH512();
    }

    @Override
    public PooledObject<JH512> wrap(JH512 t) {
         return new DefaultPooledObject<>(t);
    }
    
}
