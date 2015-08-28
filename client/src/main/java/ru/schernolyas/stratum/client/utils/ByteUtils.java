/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.utils;

import java.io.IOException;
import java.util.logging.Logger;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author Сергей
 */
public class ByteUtils {
    private static final Logger LOG = Logger.getLogger(ByteUtils.class.getName());
    /**
     * 
     * @param in
     * @see http://www.bogotobogo.com/Embedded/Little_endian_big_endian_htons_htonl.php
     * @return 
     */
    public  byte[] littleEndian(byte[] in)  {
        byte[] reversed = new byte[in.length];
        int reverse = in.length-1;
        int forward = 0;
        for (; reverse >= 0; reverse--) {
            reversed[reverse]=in[forward];
            forward++;
        }
        return reversed;
    }
    /**
     * reverse bytes like hex symbols
     * @param in array of byte with hex data
     * @return retult
     * @throws DecoderException  if hex data is incorrect
     */
    
    public byte[] swapOrder(byte[] in) throws DecoderException  {      
        StringBuilder builder = new StringBuilder(Hex.encodeHexString(in)).reverse();        
        return Hex.decodeHex(builder.toString().toCharArray());
    }
    /*
    public  byte[] preparePrevHash(byte[] prevHashButes) throws IOException  {
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
    } */
}
