/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.utils;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author Sergey Chernolyas
 */
public class NonceTimeUtil {

    private static final Logger LOG = Logger.getLogger(NonceTimeUtil.class.getName());
    public static final long MAX_NONCE = 0xFFFFFFFF;

    private Long nTime;
    private AtomicLong nonce;

    public NonceTimeUtil(byte[] nTime) throws DecoderException {
        this.nTime = new BigInteger(nTime).longValue();
        this.nonce = new AtomicLong(0L);
    }

    public NonceTimeUtil(byte[] nTime, long startNonce) throws DecoderException {
        this.nTime = new BigInteger(nTime).longValue();
        //5628506L
        this.nonce = new AtomicLong(startNonce);
    }

    public byte[] getNonce(boolean needIncrement) {
        long currentNonceValue =needIncrement ? nonce.incrementAndGet() :  nonce.get();        
        byte[] resultNonceBytes = new byte[]{0, 0, 0, 0};
        byte[] nonceIntBytes = ByteBuffer.allocate(Long.BYTES).putLong(currentNonceValue).array();
        System.arraycopy(nonceIntBytes, 4, resultNonceBytes, 0, 4);
        return resultNonceBytes;
    }

    public byte[] getNTime() {
        byte[] nTimeBytes = new byte[]{0, 0, 0, 0};
        byte[] nTimeIntBytes = ByteBuffer.allocate(Long.BYTES).putLong(nTime).array();
        System.arraycopy(nTimeIntBytes, 4, nTimeBytes, 0, 4);
        return nTimeBytes;
    }

}
