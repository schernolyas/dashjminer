/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.net;

import io.vertx.core.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author schernolyas
 */
public class ErrorHandler implements Handler<Throwable> {
    private static final Logger LOG = Logger.getLogger(ErrorHandler.class.getName());
    

    @Override
    public void handle(Throwable e) {
        LOG.log(Level.SEVERE, "ERROR:", e);
    }
    
}
