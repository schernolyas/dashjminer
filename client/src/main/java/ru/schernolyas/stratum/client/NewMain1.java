/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Сергей
 */
public class NewMain1 {
    private static final Logger LOG = Logger.getLogger(NewMain1.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //LogManager.getLogManager().readConfiguration(<ваш класс>.class.getResourceAsStream("logging.properties"));
        // Programmatic configuration
            System.setProperty("java.util.logging.SimpleFormatter.format",
                    "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL %4$-7s [%3$s] (%2$s) %5$s %6$s%n");

        try {
            String host = "mine3.coinmine.pl";
            int port = 16090; 
            /*String host = "dash.coinobox.com";
            int port = 7777; */
            Socket clientSocket = new Socket(host, port);
            clientSocket.setReceiveBufferSize(1024);
            clientSocket.setSendBufferSize(1024);
            LOG.log(Level.INFO, " connecting to {0}:{1}", new Object[]{host, String.valueOf(port)});
            LOG.log(Level.INFO, " connected: {0}", new Object[]{clientSocket.isConnected()});
            InputStreamReader reader = new InputStreamReader(clientSocket.getInputStream(), "UTF-8");  
            OutputStreamWriter writer= new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8");
            String str1="{\"id\": 1, \"method\": \"mining.subscribe\", \"params\": []}\n";
            writer.write(str1, 0, str1.length());
            writer.flush();
            BufferedReader inFromServer = new BufferedReader(reader);
            LOG.log(Level.INFO, "1. read from server: {0}", new Object[]{inFromServer.readLine()});
            String str2="{\"id\": 2, \"method\": \"mining.authorize\", \"params\": [\"sergey461\",\"Sc27071977\"]}\n";
            writer.write(str2, 0, str2.length());
            writer.flush();
            LOG.log(Level.INFO, "2. read from server: {0}", new Object[]{inFromServer.readLine()});
            LOG.log(Level.INFO, "3. read from server: {0}", new Object[]{inFromServer.readLine()});
            LOG.log(Level.INFO, "4. read from server: {0}", new Object[]{inFromServer.readLine()});
            inFromServer.close();
            writer.close();
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
