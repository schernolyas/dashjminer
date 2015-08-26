/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.utils;

import java.math.BigInteger;

/**
 *
 * @author Сергей
 */
public class NonceTimeUtil {
    private BigInteger nTime;
    private BigInteger nonce=new BigInteger(new byte[]{1,1,1,1});
    private boolean nonceOverflow;

    public NonceTimeUtil(byte[] nTime) {
        this.nTime = new BigInteger(nTime);
        this.nonce = BigInteger.ZERO;        
    }

    public void incNonce() {
        nonce = nonce.add(BigInteger.ONE);
        if (nonce.bitLength()>4) {
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
        byte[] nTimeBytes = new byte[]{0,0,0,0};
        byte[] nTimeIntBytes = nTime.toByteArray();
        System.arraycopy(nTimeIntBytes, 0, nTimeBytes, 4-nTimeIntBytes.length, nTimeIntBytes.length);
        return nTimeBytes;
    }

    public boolean isNonceOverflow() {
        return nonceOverflow;
    }
    
    
}
