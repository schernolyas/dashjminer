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
import jdk.nashorn.internal.objects.Global;
import org.apache.commons.codec.DecoderException;
import ru.schernolyas.stratum.client.minimg.GlobalObjects;

/**
 *
 * @author Sergey Chernolyas
 */
public class NonceTimeHolderImpl implements NonceTimeHolder {

    private static final Logger LOG = Logger.getLogger(NonceTimeHolderImpl.class.getName());
    public static final long MAX_NONCE = 0xFFFFFFFF;

    private Long nTime;
    private AtomicLong nonce;

    public NonceTimeHolderImpl()  {

        this.nTime = getCurrentTime();
        this.nonce = new AtomicLong(2000000L);
    }

    public NonceTimeHolderImpl(byte[] nTime) throws DecoderException {
        this.nTime = new BigInteger(nTime).longValue();
        this.nonce = new AtomicLong(0L);
    }

    public NonceTimeHolderImpl(byte[] nTime, long startNonce) throws DecoderException {
        this.nTime = new BigInteger(nTime).longValue();
        //5628506L
        this.nonce = new AtomicLong(startNonce);
    }

    public byte[] getNonce(boolean isTestMode) {
        long currentNonceValue = isTestMode ?  nonce.get() : nonce.incrementAndGet();
        byte[] resultNonceBytes = new byte[]{0, 0, 0, 0};
        byte[] nonceIntBytes = ByteBuffer.allocate(Long.BYTES).putLong(currentNonceValue).array();
        System.arraycopy(nonceIntBytes, 4, resultNonceBytes, 0, 4);
        return resultNonceBytes;
    }

    public byte[] getNTime(boolean isTestMode) {
        byte[] nTimeBytes = new byte[]{0, 0, 0, 0};
        byte[] nTimeIntBytes = ByteBuffer.allocate(Long.BYTES).
                putLong(isTestMode ? nTime : getCurrentTime()).array();
        System.arraycopy(nTimeIntBytes, 4, nTimeBytes, 0, 4);
        return nTimeBytes;
    }

    @Override
    public byte[] getNTime() {
        return getNTime(GlobalObjects.isTestMode());
    }

    @Override
    public byte[] getNonce() {
        return getNonce(GlobalObjects.isTestMode());
    }
    

    private long getCurrentTime() {
        long miliseconds = System.currentTimeMillis();
        long seconds = miliseconds / 1000;
        return seconds;
    }

}
