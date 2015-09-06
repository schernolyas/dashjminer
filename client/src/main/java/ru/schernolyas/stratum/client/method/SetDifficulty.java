/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.method;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        setDifficulty.setDecimalDifficulty(new BigDecimal(paramsArray.getDouble(0)));
        LOG.log(Level.INFO, "current difficulty : 1) byte: {0}; 2) decimal: {1}; {2}",
                    new Object[]{paramsArray.getDouble(0),setDifficulty.getDecimalDifficulty(),
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
        JsonObject obj = new JsonObject(jsonString);
        return processJsonObject(obj);
    }

}
