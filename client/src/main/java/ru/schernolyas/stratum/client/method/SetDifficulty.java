/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.method;

import java.math.BigDecimal;
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
public class SetDifficulty {

    private static final Logger LOG = Logger.getLogger(SetDifficulty.class.getName());

    private String inJson;
    private JsonObject jsonObject;

    private byte[] byteDifficulty;
    private BigDecimal decimalDifficulty;

    public SetDifficulty(String inJson, JsonObject jsonObject) throws DecoderException {
        this.inJson = inJson;
        this.jsonObject = jsonObject;
        processJsonObject();

    }

    private void processJsonObject() throws DecoderException {
        JsonArray paramsArray = this.jsonObject.getJsonArray("params");
        decimalDifficulty = paramsArray.getJsonNumber(0).bigDecimalValue();
        LOG.log(Level.INFO, "current difficulty : 1) byte: {0}; 2) decimal: {1}; {2}",
                    new Object[]{paramsArray.getJsonNumber(0).bigDecimalValue(),decimalDifficulty,
                    Math.getExponent(decimalDifficulty.doubleValue())});
        String str = Integer.toHexString(Float.floatToRawIntBits(decimalDifficulty.floatValue()));
        this.byteDifficulty = Hex.decodeHex(str.toCharArray());
    }

    public byte[] getByteDifficulty() {
        return byteDifficulty;
    }

    public BigDecimal getDecimalDifficulty() {
        return decimalDifficulty;
    }

}
