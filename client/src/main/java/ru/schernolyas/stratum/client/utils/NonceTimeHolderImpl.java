/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.utils;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;
import org.apache.commons.codec.DecoderException;

/**
 *
 * @author Sergey Chernolyas
 */
public class NonceTimeHolderImpl implements NonceTimeHolder {

    private static final Logger LOG = Logger.getLogger(NonceTimeHolderImpl.class.getName());
    public static final BigInteger MAX_NONCE = new BigInteger("FFFFFFFF", 16);

    private Long nTime;
    private AtomicLong nonce;

    public NonceTimeHolderImpl()  {

        this.nTime = getCurrentTimeInSeconds();
        this.nonce = new AtomicLong(-1L);
    }

    public NonceTimeHolderImpl(byte[] nTime) throws DecoderException {
        this.nTime = new BigInteger(nTime).longValue();
        this.nonce = new AtomicLong(0L);
    }

    public NonceTimeHolderImpl(byte[] nTime, long startNonce) throws DecoderException {
        this.nTime = new BigInteger(nTime).longValue();
        this.nonce = new AtomicLong(startNonce);
    }
    public NonceTimeHolderImpl(long startNonce) throws DecoderException {
        this.nonce = new AtomicLong(startNonce);
    }

    @Override
    public byte[] getNonce() {
        return getNonce(0);
    }

    @Override
    public byte[] getNonce(long offset) {
        long currentNonce = nonce.incrementAndGet()+offset;
        return Arrays.copyOfRange(ByteBuffer.allocate(Long.BYTES).putLong(currentNonce).array(),4, 8) ;
    }
    

    @Override
    public byte[] getNTime() {
        byte[] nTimeBytes = new byte[]{0, 0, 0, 0};
        /*byte[] nTimeIntBytes = ByteBuffer.allocate(Long.BYTES).
                putLong(getCurrentTimeInSeconds()).array();
        System.arraycopy(nTimeIntBytes, 4, nTimeBytes, 0, 4);*/
        return nTimeBytes;
    }

    private long getCurrentTimeInSeconds() {
        long miliseconds = System.currentTimeMillis();
        long seconds = miliseconds / 1000;
        return seconds;
    }

}
