/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.method;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author Sergey Chernolyas
 */
public class SetDifficulty {

    private static final Logger LOG = Logger.getLogger(SetDifficulty.class.getName());

    private byte[] byteDifficulty;
    private BigDecimal decimalDifficulty;

    private static SetDifficulty processJsonObject(JsonObject jsonObject) throws DecoderException {
        SetDifficulty setDifficulty = new SetDifficulty();
        JsonArray paramsArray = jsonObject.getJsonArray("params");
        setDifficulty.setDecimalDifficulty(paramsArray.getJsonNumber(0).bigDecimalValue());
        LOG.log(Level.INFO, "current difficulty : 1) byte: {0}; 2) decimal: {1}; {2}",
                    new Object[]{paramsArray.getJsonNumber(0).bigDecimalValue(),setDifficulty.getDecimalDifficulty(),
                    Math.getExponent(setDifficulty.getDecimalDifficulty().doubleValue())});
        String str = Integer.toHexString(Float.floatToRawIntBits(setDifficulty.getDecimalDifficulty().floatValue()));
        setDifficulty.setByteDifficulty(Hex.decodeHex(str.toCharArray()));
        return setDifficulty;
    }

    public byte[] getByteDifficulty() {
        return byteDifficulty;
    }

    public void setByteDifficulty(byte[] byteDifficulty) {
        this.byteDifficulty = byteDifficulty;
    }

    public BigDecimal getDecimalDifficulty() {
        return decimalDifficulty;
    }

    public void setDecimalDifficulty(BigDecimal decimalDifficulty) {
        this.decimalDifficulty = decimalDifficulty;
    }

    
    
    public static SetDifficulty build(String jsonString) throws DecoderException {
        JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
        JsonObject obj = jsonReader.readObject();
        return processJsonObject(obj);
    }

}
