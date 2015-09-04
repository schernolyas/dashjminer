/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.impl.VertxImpl;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author schernolyas
 */
public class NewMain2  {

    private static final Logger LOG = Logger.getLogger(NewMain2.class.getName());
    private int answerCount = 0;
    private final String str1="{\"id\": 1, \"method\": \"mining.subscribe\", \"params\": []}\n";
    private final String str2="{\"id\": 2, \"method\": \"mining.authorize\", \"params\": [\"********\",\"*****\"]}\n";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        NewMain2 m = new NewMain2();
        m.start();

    }

    public void start() throws Exception {
        LOG.log(Level.INFO, "start vert.x");
        Vertx vertx = Vertx.vertx();
        NetClientOptions options = new NetClientOptions();
        options.setReconnectAttempts(10).
                setReconnectInterval(500).
                setReceiveBufferSize(1024 * 10).
                setTcpKeepAlive(true);
        
        NetClient tcpClient = vertx.createNetClient(options);
        Handler<AsyncResult<NetSocket>> handler = new Handler<AsyncResult<NetSocket>>() {

            @Override
            public void handle(AsyncResult<NetSocket> result) {
                final NetSocket socket = result.result();

                Handler<Throwable> exceptionHandler = new Handler<Throwable>() {

                    @Override
                    public void handle(Throwable e) {
                        e.printStackTrace();
                    }
                };

                Handler<Buffer> readSocketHandler = new Handler<Buffer>() {

                    @Override
                    public void handle(Buffer buffer) {
                        try {
                            String str = new String(buffer.getBytes(), "UTF-8");
                            System.out.println("read from socket: "+str);
                            System.out.println("NewMain2.this.answerCoun: "+NewMain2.this.answerCount);
                            if (NewMain2.this.answerCount==0) {
                                socket.write(str2);
                                NewMain2.this.answerCount++;
                            };
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                };
                socket.closeHandler(new Handler<Void>() {

                    @Override
                    public void handle(Void e) {
                        System.out.println("Socket closed");
                    }
                });
                
                socket.write(str1);

                socket.exceptionHandler(exceptionHandler);
                socket.handler(readSocketHandler);

            }
        };

        tcpClient.connect(16090, "mine3.coinmine.pl", handler);

    }

}
