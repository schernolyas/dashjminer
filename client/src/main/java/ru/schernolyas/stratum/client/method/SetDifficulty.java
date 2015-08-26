/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.method;

import java.math.BigDecimal;
import javax.json.JsonArray;
import javax.json.JsonObject;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author Сергей
 */
public class SetDifficulty {
    private String inJson;
    private JsonObject jsonObject;
    
    private byte[] difficulty;
    
    public SetDifficulty(String inJson, JsonObject jsonObject) throws DecoderException {
        this.inJson = inJson;
        this.jsonObject = jsonObject;
        processJsonObject();

    }

    private void processJsonObject() throws DecoderException {
        JsonArray paramsArray = this.jsonObject.getJsonArray("params");
        BigDecimal nd = paramsArray.getJsonNumber(0).bigDecimalValue().setScale(5);
        String str = Integer.toHexString(Float.floatToRawIntBits(nd.floatValue()));
        this.difficulty = Hex.decodeHex(str.toCharArray());
    }

    public byte[] getDifficulty() {
        return difficulty;
    }
    
    
}
