/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.utils;

import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author Сергей
 */
public class DifficultyUtil {
    private static final Logger LOG = Logger.getLogger(DifficultyUtil.class.getName());   
     
    public static BigInteger calculateTarget(byte[] currentDifficultyBytes) throws DecoderException {
        byte[] b = Hex.decodeHex("00000FFFF0000000000000000000000000000000000000000000000000000000".toCharArray());
        return new BigInteger(b).divide(new BigInteger(currentDifficultyBytes));
    }
    
}
