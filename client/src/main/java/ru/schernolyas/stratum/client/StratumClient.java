/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.schernolyas.stratum.client.minimg.GlobalObjects;
import ru.schernolyas.stratum.client.minimg.MiningManager;
import ru.schernolyas.stratum.client.net.Consumers;
import ru.schernolyas.stratum.client.net.MediatorThread;
import ru.schernolyas.stratum.client.net.NetHandler;

/**
 *
 * @author schernolyas
 */
public class StratumClient {

    private static final Logger LOG = Logger.getLogger(StratumClient.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        StratumClient m = new StratumClient();
        m.start();

    }

    public void start() throws Exception {
        LOG.log(Level.INFO, "start vert.x");
        Vertx vertx = Vertx.vertx();
        final EventBus eventBus = vertx.eventBus();
        NetClientOptions options = new NetClientOptions();
        options.setReconnectAttempts(10).
                setReconnectInterval(500).
                setReceiveBufferSize(1024 * 10).
                setTcpKeepAlive(true);

        NetClient tcpClient = vertx.createNetClient(options);
        MiningManager miningManager = new MiningManager(eventBus);
        miningManager.start();

        eventBus.consumer(Consumers.NOTIFY, new Handler<Message<String>>() {

            @Override
            public void handle(Message<String> message) {
                LOG.log(Level.INFO, "get message of type 'mining.notify': {0}", new Object[]{message.body()});
                GlobalObjects.addNewMiningNotify(message.body());
            }
        });
        eventBus.consumer(Consumers.SET_DIFFICULTY, new Handler<Message<String>>() {

            @Override
            public void handle(Message<String> message) {
                LOG.log(Level.INFO, "get message of type 'mining.set_difficulty': {0}", new Object[]{message.body()});
                GlobalObjects.setSetDifficulty(message.body());
            }
        });

        tcpClient.connect(16090, "mine3.coinmine.pl", new NetHandler(eventBus));

    }

}
