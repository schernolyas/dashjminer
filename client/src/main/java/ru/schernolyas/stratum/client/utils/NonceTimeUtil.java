/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.utils;

import java.math.BigInteger;
import java.util.logging.Logger;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author Sergey Chernolyas
 */
public class NonceTimeUtil {

    private static final Logger LOG = Logger.getLogger(NonceTimeUtil.class.getName());

    private BigInteger nTime;
    private BigInteger nonce = BigInteger.ONE;
    private boolean nonceOverflow;

    public NonceTimeUtil(byte[] nTime) throws DecoderException {
        this.nTime = new BigInteger(nTime);
        this.nonce = new BigInteger(Hex.decodeHex(Long.toHexString(5628506).toCharArray()));
    }

    public void incNonce() {
        nonce = nonce.add(BigInteger.ONE);
        if (nonce.bitLength() > 4) {
            nTime = nTime.add(BigInteger.ONE);
        }
    }

    public byte[] getNonce() {
        byte[] nonceBytes = new byte[]{0,0,0,0};
        byte[] nonceIntBytes = nonce.toByteArray();
        System.arraycopy(nonceIntBytes, 0, nonceBytes, 4-nonceIntBytes.length, nonceIntBytes.length);
        return nonceBytes;
    }

    public byte[] getNTime() {
        byte[] nTimeBytes = new byte[]{0, 0, 0, 0};
        byte[] nTimeIntBytes = nTime.toByteArray();
        if (nTimeIntBytes.length > 4) {
            int startPos = nTimeBytes.length-4;
            System.arraycopy(nTimeIntBytes, 0, nTimeBytes, startPos, 4);
        } else {
            System.arraycopy(nTimeIntBytes, 0, nTimeBytes, 4 - nTimeIntBytes.length, nTimeIntBytes.length);
        }
        return nTimeBytes;
    }

    public boolean isNonceOverflow() {
        return nonceOverflow;
    }

}
