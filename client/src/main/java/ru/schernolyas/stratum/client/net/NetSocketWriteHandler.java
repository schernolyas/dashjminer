/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.net;

import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.net.NetSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author schernolyas
 */
public class NetSocketWriteHandler implements Handler<Message<String>> {
    private static final Logger LOG = Logger.getLogger(NetSocketWriteHandler.class.getName());
    
   private  NetSocket socket;

    public NetSocketWriteHandler(NetSocket socket) {
        this.socket = socket;
    }
   

    @Override
    public void handle(Message<String> message) {
        LOG.log(Level.INFO, "write message");
        socket.write(message.body());
    }
    
}
