/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author Sergey Chernolyas
 */
public class ByteUtils {

    private static final Logger LOG = Logger.getLogger(ByteUtils.class.getName());

    /**
     *
     * @param in
     * @see
     * http://www.bogotobogo.com/Embedded/Little_endian_big_endian_htons_htonl.php
     * @return
     */
    public static byte[] littleEndian(byte[] in) {
        byte[] reversed = new byte[in.length];
        int reverse = in.length - 1;
        int forward = 0;
        for (; reverse >= 0; reverse--) {
            reversed[reverse] = in[forward];
            forward++;
        }
        return reversed;
    }

    public static byte[] bigEndian(byte[] in) {
        byte[] reversed = new byte[in.length];
        int reverse = in.length - 1;
        int forward = 0;
        for (; reverse >= 0; reverse--) {
            reversed[reverse] = in[forward];
            forward++;
        }
        return reversed;
    }

    /**
     * TODO: redevelop!!!! reverse bytes like hex symbols
     *
     * @param in array of byte with hex data
     * @return result
     * @throws DecoderException if hex data is incorrect
     */

    public static byte[] swapOrder(byte[] in) throws DecoderException {
        StringBuilder builder = new StringBuilder(Hex.encodeHexString(in)).reverse();
        return Hex.decodeHex(builder.toString().toCharArray());
    }

    public static BigInteger toBigInteger(byte[] bytes) {
        return new BigInteger(1, bytes);
    }

    public static byte[] concat(byte[]... arrays) throws IOException {
        Pipe arraysConcatPipe = Pipe.open();
        Pipe.SinkChannel writeChannel = arraysConcatPipe.sink();
        Pipe.SourceChannel readChannel = arraysConcatPipe.source();
        int wrireBytes = 0;
        for (byte[] array : arrays) {
            wrireBytes = wrireBytes + writeChannel.write(ByteBuffer.wrap(array));
        }
        ByteBuffer resultBuffer = ByteBuffer.allocate(wrireBytes);
        int readBytes = readChannel.read(resultBuffer);
        return resultBuffer.array();
    }

    /**
     *
     * @param array1
     * @param array2
     * @return 1 if array1>array2, 0 if array1=array2 and -1 if array1<array2
     */

    public static int fastCompare(byte[] array1, byte[] array2) {
        int result = 0;
        for (int index = 0; (index < array1.length && result == 0); index++) {
            int v1 = toUnsignedByte(array1[index]);
            int v2 = toUnsignedByte(array2[index]);
            boolean greater = (v1 > v2);
            boolean less = (v1 < v2);
            //System.out.println("index:"+index+";"+v1+";"+v2+";"+greater+";"+less);
            if (greater) {
                result = 1;
            } else if (less) {
                result = -1;
            }
        }
        return result;
    }

    public static int toUnsignedByte(byte b) {
        return b > 0 ? b : b & 0xff;
    }

    public static byte[] extend(byte[] array, int size) {
        byte[] resultArray = new byte[size];
        Arrays.fill(resultArray, 0, (size - array.length), (byte) 0);
        System.arraycopy(array, 0, resultArray, (size - array.length), array.length);
        return resultArray;
    }

    /**
     * <code>
     * // Assemble block header 
     *	work->data[0] = le32dec(sctx->job.version);
     *  for (i = 0; i < 8; i++) {
     *     work->data[1 + i] = le32dec((uint32_t *)sctx->job.prevhash + i); 
     * }
     * for (i =0; i < 8; i++) {
     *     work->data[9 + i] = be32dec((uint32_t *)merkle_root + i); 
     * }
     * work->data[17] = le32dec(sctx->job.ntime); 
     * work->data[18] = le32dec(sctx->job.nbits);
     * work->data[20] = 0x80000000; 
     * work->data[31] = 0x00000280; 
     * </code>
     *
     * @param prevHashButes
     * @return
     * @throws IOException
     */
    public static byte[] preparePrevHash(byte[] prevHashButes) throws IOException {
        LOG.log(Level.INFO, "preparePrevHash in : {0}", new Object[]{Hex.encodeHexString(prevHashButes)});
        ByteArrayOutputStream baos = new ByteArrayOutputStream(32);
        for (int position = 0; position < 32; position += 4) {
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
