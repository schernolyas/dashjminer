/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.cryptopool;

import fr.cryptohash.BMW512;
import fr.cryptohash.Keccak512;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 *
 * @author schernolyas
 */
public class Keccak512Factory extends BasePooledObjectFactory<Keccak512> {
    private static final Logger LOG = Logger.getLogger(Keccak512Factory.class.getName());
    
    @Override
    public Keccak512 create() throws Exception { 
        return new Keccak512();
    }

    @Override
    public PooledObject<Keccak512> wrap(Keccak512 t) {
         return new DefaultPooledObject<>(t);
    }
    
}
