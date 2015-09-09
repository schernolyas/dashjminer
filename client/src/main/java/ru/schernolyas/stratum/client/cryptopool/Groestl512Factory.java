/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.cryptopool;

import fr.cryptohash.Groestl512;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * Groestl512
 * @author schernolyas
 */
public class Groestl512Factory extends BasePooledObjectFactory<Groestl512> {
    private static final Logger LOG = Logger.getLogger(Groestl512Factory.class.getName());
    
    @Override
    public Groestl512 create() throws Exception { 
        return new Groestl512();
    }

    @Override
    public PooledObject<Groestl512> wrap(Groestl512 t) {
         return new DefaultPooledObject<>(t);
    }
    
}
