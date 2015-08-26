/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author Сергей
 */
public class ByteUtils {
    private static final Logger LOG = Logger.getLogger(ByteUtils.class.getName());
    
    public static byte[] littleEndian(byte[] in)  {
        //LOG.log(Level.INFO, "littleEndian in : {0}", new Object[]{Hex.encodeHexString(in)});
        byte[] reversed = new byte[in.length];
        int reverse = in.length-1;
        int forward = 0;
        for (; reverse >= 0; reverse--) {
            reversed[reverse]=in[forward];
            forward++;
        }
        //LOG.log(Level.INFO, "littleEndian reversed : {0}", new Object[]{Hex.encodeHexString(reversed)});
        return reversed;
    }
    
    public static byte[] preparePrevHash(byte[] prevHashButes) throws IOException  {
        LOG.log(Level.INFO, "preparePrevHash in : {0}", new Object[]{Hex.encodeHexString(prevHashButes)});
        ByteArrayOutputStream baos = new ByteArrayOutputStream(32);
        for (int position=0;position<32;position+=4) {
            byte[] portion = new byte[4];
            LOG.log(Level.INFO, "current position  : {0}", new Object[]{position});
            System.arraycopy(prevHashButes, position, portion, 0, 4);
            baos.write(littleEndian(portion));
        }
        baos.flush();
        LOG.log(Level.INFO, "prepared PrevHash : {0}", new Object[]{Hex.encodeHexString(baos.toByteArray())});
        return baos.toByteArray();
    }
}
