/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author schernolyas
 */
public class NewMain2 extends AbstractVerticle {

    private static final Logger LOG = Logger.getLogger(NewMain2.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        NewMain2 m = new NewMain2();

    }

    @Override
    public void start() throws Exception {
        LOG.log(Level.INFO, "start vert.x");
        NetClientOptions options = new NetClientOptions();
        options.setReconnectAttempts(10).
                setReconnectInterval(500).
                setReceiveBufferSize(1024*10).
                setTcpKeepAlive(true);
        NetClient tcpClient = getVertx().createNetClient(options);
        Handler<AsyncResult<NetSocket>> handler = new Handler<AsyncResult<NetSocket>>() {

            @Override
            public void handle(AsyncResult<NetSocket> result) {
                NetSocket socket = result.result();

                Handler<Throwable> exceptionHandler = new Handler<Throwable>() {

                    @Override
                    public void handle(Throwable e) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                };

                Handler<Buffer> readSocketHandler = new Handler<Buffer>() {

                    @Override
                    public void handle(Buffer e) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                };

                socket.exceptionHandler(exceptionHandler);
                socket.handler(readSocketHandler);

            }
        };

        tcpClient.connect(80, "jenkov.com", handler);
    }

}
