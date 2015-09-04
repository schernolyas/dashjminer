/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.utils;

/**
 *
 * @author schernolyas
 */
public class ExtraNonce2Util {
    private static final byte[] DEFAULT_EXTRA_NONCE2 = new byte[]{0,0,0,0};
    
    public static byte[] createExtraNonce2(int size) {
        return DEFAULT_EXTRA_NONCE2;
    }
    
}
