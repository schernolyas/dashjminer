/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import ru.schernolyas.stratum.client.method.MiningNotify;
import ru.schernolyas.stratum.client.method.SetDifficulty;

/**
 * @see
 * http://www.javaworld.com/article/2077257/learn-java/floating-point-arithmetic.html
 * @author Sergey Chernolyas
 */
public class DifficultyUtil {

    private static final Logger LOG = Logger.getLogger(DifficultyUtil.class.getName());

    @Deprecated
    public static byte[] calculateTarget(SetDifficulty difficulty) throws DecoderException {
        return calculateTarget(difficulty.getDecimalDifficulty());
    }
    @Deprecated
    public static byte[] calculateTarget(BigDecimal decimalDifficulty) throws DecoderException {
        LOG.log(Level.INFO, "Difficulty: {0}", new Object[]{Float.toHexString(decimalDifficulty.floatValue())});
        int exponent = Math.getExponent(decimalDifficulty.floatValue());

        int targetExponent = 208 - exponent;
        BigInteger target = new BigInteger("ffff", 16).multiply(new BigInteger("2").pow(targetExponent));
        byte[] resultBytes = new byte[32];
        if (target.toByteArray().length < 32) {
            byte[] bytes = new byte[32];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = 0;
            }
            int startPos = 32 - target.toByteArray().length;
            System.arraycopy(target.toByteArray(), 0, resultBytes, startPos, target.toByteArray().length);
        }
        LOG.log(Level.INFO, "resultBytes : {0}", new Object[]{Hex.encodeHexString(resultBytes)});
        return resultBytes;
    }
    /**
     * 
     * @param nBits EncodedNetworkDifficulty from MiningNotify
     * @return
     * @throws DecoderException 
     * @see https://bitcoin.org/en/developer-reference#target-nbits
     */
    public static byte[] calculateTarget(byte[] nBits) {
        byte[] mantissaBytes = new byte[nBits.length-1];
        int mantissaByteCount = mantissaBytes.length;
        System.arraycopy(nBits, 1, mantissaBytes, 0, mantissaBytes.length);
        int exponent = nBits[0];
        return new BigInteger(mantissaBytes).multiply(
                new BigInteger("256",10).pow(exponent-mantissaByteCount)).toByteArray();
        
    }

}
