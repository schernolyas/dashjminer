/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.net;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetSocket;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.schernolyas.stratum.client.StratumClient;

/**
 *
 * @author schernolyas
 */
public class NetHandler implements Handler<AsyncResult<NetSocket>> {

    private final String str1 = "{\"id\": 1, \"method\": \"mining.subscribe\", \"params\": []}\n";
    private final String str2 = "{\"id\": 2, \"method\": \"mining.authorize\", \"params\": [\"********\",\"*****\"]}\n";
    private static final Logger LOG = Logger.getLogger(NetHandler.class.getName());
    private boolean needWaitAnswer = false;
    private EventBus eventBus;

    public NetHandler(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void handle(AsyncResult<NetSocket> result) {
        final NetSocket socket = result.result();

        Handler<Buffer> readSocketHandler = new Handler<Buffer>() {

            @Override
            public void handle(Buffer buffer) {
                try {
                    String stringFromSocket = new String(buffer.getBytes(), "UTF-8");
                    LOG.log(Level.INFO, "read from socket: {0}", new Object[]{stringFromSocket});
                    if (needWaitAnswer) {
                        socket.write(str2);
                        needWaitAnswer = false;
                    };

                    String[] commands = stringFromSocket.split("[\\n]+");
                    for (String command : commands) {
                        JsonObject object = new JsonObject(command);
                        if (object.containsKey("method")) {
                            String methodName = object.getString("method");
                            LOG.log(Level.INFO, "send message : {0} to listener: {1}", new Object[]{command,methodName});
                            eventBus.publish(methodName,command);
                        }

                    }

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
        needWaitAnswer = true;
        eventBus.consumer(Consumers.WRITE_TO_SOCKET, new NetSocketWriteHandler(socket));

        socket.exceptionHandler(new ErrorHandler());
        socket.handler(readSocketHandler);
    }
    
    

}
