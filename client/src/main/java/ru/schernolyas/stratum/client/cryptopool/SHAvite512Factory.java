/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.cryptopool;

import fr.cryptohash.SHAvite512;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 *
 * @author schernolyas
 */
public class SHAvite512Factory extends BasePooledObjectFactory<SHAvite512> {
    private static final Logger LOG = Logger.getLogger(SHAvite512Factory.class.getName());
    
    @Override
    public SHAvite512 create() throws Exception {        
        LOG.log(Level.INFO, "create object");
        return new SHAvite512();
    }

    @Override
    public PooledObject<SHAvite512> wrap(SHAvite512 t) {
         return new DefaultPooledObject<>(t);
    }
    
}
