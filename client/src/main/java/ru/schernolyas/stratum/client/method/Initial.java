/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.method;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author Sergey Chernolyas
 */
public class Initial {

    private static final Logger LOG = Logger.getLogger(Initial.class.getName());

    private byte[] miningNotify;
    private byte[] miningSetDifficulty;
    private byte[] extraNonce1;
    private int extraNonce2Size;

    
    private static Initial processJsonObject(JsonObject jsonObject) throws DecoderException {
        Initial initial = new Initial();
        JsonArray resultArray = jsonObject.getJsonArray("result");
        JsonArray array1 = resultArray.getJsonArray(0);
        for (int i = 0; i < array1.size(); i=+2) {
            
           
            String name = array1.getString(i);
            String value = array1.getString(i+1);
            if (name.equalsIgnoreCase("mining.notify")) {
                initial.setMiningNotify(Hex.decodeHex(value.toCharArray()));
            } 
        }
        LOG.log(Level.INFO, "miningNotify : {0}", new Object[]{Hex.encodeHexString(initial.getMiningNotify())});
        initial.setExtraNonce1(Hex.decodeHex(resultArray.getString(1).toCharArray()));
        LOG.log(Level.INFO, "extraNonce1 : {0}", new Object[]{Hex.encodeHexString(initial.getExtraNonce1())});
        initial.setExtraNonce2Size(resultArray.getInteger(2));
        LOG.log(Level.INFO, "extraNonce2Size : {0}", new Object[]{initial.getExtraNonce2Size()});
        return initial;
    }

    public byte[] getMiningNotify() {
        return miningNotify;
    }

    public void setMiningNotify(byte[] miningNotify) {
        this.miningNotify = miningNotify;
    }

    public byte[] getMiningSetDifficulty() {
        return miningSetDifficulty;
    }

    public void setMiningSetDifficulty(byte[] miningSetDifficulty) {
        this.miningSetDifficulty = miningSetDifficulty;
    }

    public byte[] getExtraNonce1() {
        return extraNonce1;
    }

    public void setExtraNonce1(byte[] extraNonce1) {
        this.extraNonce1 = extraNonce1;
    }

    public int getExtraNonce2Size() {
        return extraNonce2Size;
    }

    public void setExtraNonce2Size(int extraNonce2Size) {
        this.extraNonce2Size = extraNonce2Size;
    }

    public static Initial build(String jsonString) throws DecoderException {        
        JsonObject obj = new JsonObject(jsonString);
        return processJsonObject(obj);
    }

}
