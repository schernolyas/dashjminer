/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author Сергей
 */
public class X11Util {
    private static final Logger LOG = Logger.getLogger(X11Util.class.getName());
    
    public static byte[] calculate(byte[] inHash) {
        byte[] hash = new fr.cryptohash.BLAKE512().digest(inHash);
        LOG.log(Level.INFO, "BLAKE512 : {0}", new Object[]{Hex.encodeHexString(hash)});
        hash = new fr.cryptohash.BMW512().digest(hash);
        LOG.log(Level.INFO, "BMW512 : {0}", new Object[]{Hex.encodeHexString(hash)});
        hash = new fr.cryptohash.Groestl512().digest(hash);
        LOG.log(Level.INFO, "Groestl512 : {0}", new Object[]{Hex.encodeHexString(hash)});
        hash = new fr.cryptohash.Skein512().digest(hash);
        LOG.log(Level.INFO, "Skein512 : {0}", new Object[]{Hex.encodeHexString(hash)});
        hash = new fr.cryptohash.JH512().digest(hash);
        LOG.log(Level.INFO, "JH512 : {0}", new Object[]{Hex.encodeHexString(hash)});
        hash = new fr.cryptohash.Keccak512().digest(hash);
        LOG.log(Level.INFO, "Keccak512 : {0}", new Object[]{Hex.encodeHexString(hash)});
        hash = new fr.cryptohash.Luffa512().digest(hash);
        LOG.log(Level.INFO, "Luffa512 : {0}", new Object[]{Hex.encodeHexString(hash)});
        hash = new fr.cryptohash.CubeHash512().digest(hash);
        LOG.log(Level.INFO, "CubeHash512 : {0}", new Object[]{Hex.encodeHexString(hash)});
        hash = new fr.cryptohash.SHAvite512().digest(hash);
        LOG.log(Level.INFO, "SHAvite512 : {0}", new Object[]{Hex.encodeHexString(hash)});
        hash = new fr.cryptohash.SIMD512().digest(hash);
        LOG.log(Level.INFO, "SIMD512 : {0}", new Object[]{Hex.encodeHexString(hash)});
        hash = new fr.cryptohash.ECHO512().digest(hash);
        LOG.log(Level.INFO, "ECHO512 : {0}", new Object[]{Hex.encodeHexString(hash)});
        return hash;
    }
}
