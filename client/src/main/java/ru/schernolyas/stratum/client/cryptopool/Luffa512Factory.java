/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.cryptopool;

import fr.cryptohash.BMW512;
import fr.cryptohash.Luffa512;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 *
 * @author schernolyas
 */
public class Luffa512Factory extends BasePooledObjectFactory<Luffa512> {
    private static final Logger LOG = Logger.getLogger(Luffa512Factory.class.getName());
    
    @Override
    public Luffa512 create() throws Exception {  
        return new Luffa512();
    }

    @Override
    public PooledObject<Luffa512> wrap(Luffa512 t) {
         return new DefaultPooledObject<>(t);
    }
    
}
