/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.method;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonArray;
import javax.json.JsonObject;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author Sergey Chernolyas
 */
public class Initial {

    private static final Logger LOG = Logger.getLogger(Initial.class.getName());

    private String inJson;
    private JsonObject jsonObject;

    private byte[] miningNotify;
    private byte[] miningSetDifficulty;
    private byte[] extraNonce1;
    private int extraNonce2Size;
    private byte[] extraNonce2;

    public Initial(String inJson, JsonObject jsonObject) throws DecoderException {
        this.inJson = inJson;
        this.jsonObject = jsonObject;
        processJsonObject();
    }

    private void processJsonObject() throws DecoderException {
        JsonArray resultArray = this.jsonObject.getJsonArray("result");
        JsonArray array1 = resultArray.getJsonArray(0);
        for (int i = 0; i < array1.size(); i++) {
            JsonArray ar = array1.getJsonArray(i);
            String name = ar.getJsonString(0).getString();
            if (name.equalsIgnoreCase("mining.notify")) {
                this.miningNotify = Hex.decodeHex(ar.getJsonString(1).getString().toCharArray());
            }
        }
        LOG.log(Level.INFO, "miningNotify : {0}", new Object[]{Hex.encodeHexString(miningNotify)});
        this.extraNonce1 = Hex.decodeHex(resultArray.getJsonString(1).getString().toCharArray());
        LOG.log(Level.INFO, "extraNonce1 : {0}", new Object[]{Hex.encodeHexString(extraNonce1)});
        this.extraNonce2Size = resultArray.getInt(2);
        LOG.log(Level.INFO, "extraNonce2Size : {0}", new Object[]{extraNonce2Size});
        this.extraNonce2 = new byte[]{0, 0, 0, 2};
    }

    public byte[] getMiningNotify() {
        return miningNotify;
    }

    public byte[] getMiningSetDifficulty() {
        return miningSetDifficulty;
    }

    public byte[] getExtraNonce1() {
        return extraNonce1;
    }

    public int getExtraNonce2Size() {
        return extraNonce2Size;
    }

    public byte[] getExtraNonce2() {
        return extraNonce2;
    }

}
