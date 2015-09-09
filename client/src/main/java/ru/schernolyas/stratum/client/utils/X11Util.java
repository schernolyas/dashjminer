/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.utils;

import fr.cryptohash.BLAKE512;
import fr.cryptohash.BMW512;
import fr.cryptohash.CubeHash512;
import fr.cryptohash.Groestl512;
import fr.cryptohash.JH512;
import fr.cryptohash.Keccak512;
import fr.cryptohash.Luffa512;
import fr.cryptohash.Skein512;
import java.util.logging.Logger;
import org.apache.commons.pool2.ObjectPool;
import ru.schernolyas.stratum.client.cryptopool.CryptoPool;

/**
 *
 * @author Сергей
 */
public class X11Util {

    private static final Logger LOG = Logger.getLogger(X11Util.class.getName());

    public static byte[] calculate(byte[] inHash) throws Exception {

        ObjectPool<BLAKE512> blake512Pool = CryptoPool.getBlake512Pool();
        BLAKE512 blake512 = blake512Pool.borrowObject();
        byte[] hash = blake512.digest(inHash);
        blake512Pool.returnObject(blake512);
        // LOG.log(Level.INFO, "BLAKE512 : {0}", new Object[]{Hex.encodeHexString(hash)});

        ObjectPool<BMW512> bmw512Pool = CryptoPool.getBMW512Pool();
        BMW512 bmv512 = bmw512Pool.borrowObject();
        hash = bmv512.digest(hash);
        bmw512Pool.returnObject(bmv512);
        //  LOG.log(Level.INFO, "BMW512 : {0}", new Object[]{Hex.encodeHexString(hash)});

        ObjectPool<Groestl512> groestl512Pool = CryptoPool.getGroestl512Pool();
        Groestl512 groestl512 = groestl512Pool.borrowObject();
        hash = groestl512.digest(hash);
        groestl512Pool.returnObject(groestl512);
        //   LOG.log(Level.INFO, "Groestl512 : {0}", new Object[]{Hex.encodeHexString(hash)});

        ObjectPool<Skein512> skein512Pool = CryptoPool.getSkein512Pool();
        Skein512 skein512 = skein512Pool.borrowObject();
        hash = skein512.digest(hash);
        skein512Pool.returnObject(skein512);

        //  LOG.log(Level.INFO, "Skein512 : {0}", new Object[]{Hex.encodeHexString(hash)});
        ObjectPool<JH512> jh512Pool = CryptoPool.getJh512Pool();
        JH512 jh512 = jh512Pool.borrowObject();
        hash = jh512.digest(hash);
        jh512Pool.returnObject(jh512);
        //  LOG.log(Level.INFO, "JH512 : {0}", new Object[]{Hex.encodeHexString(hash)});

        ObjectPool<Keccak512> keccak512Pool = CryptoPool.getKeccak512Pool();
        Keccak512 keccak512 = keccak512Pool.borrowObject();
        hash = keccak512.digest(hash);
        keccak512Pool.returnObject(keccak512);
        //  LOG.log(Level.INFO, "Keccak512 : {0}", new Object[]{Hex.encodeHexString(hash)});
        
        ObjectPool<Luffa512> luffa512Pool = CryptoPool.getLuffa512Pool();
        Luffa512 luffa512 = luffa512Pool.borrowObject();
        hash = luffa512.digest(hash);
        luffa512Pool.returnObject(luffa512);

        //  LOG.log(Level.INFO, "Luffa512 : {0}", new Object[]{Hex.encodeHexString(hash)});
        ObjectPool<CubeHash512> cubeHash512Pool = CryptoPool.getCubeHash512Pool();
        CubeHash512 cubeHash512 = cubeHash512Pool.borrowObject();
        hash = cubeHash512.digest(hash);
        cubeHash512Pool.returnObject(cubeHash512);
        //  LOG.log(Level.INFO, "CubeHash512 : {0}", new Object[]{Hex.encodeHexString(hash)});
        
        
        hash = new fr.cryptohash.SHAvite512().digest(hash);
        //  LOG.log(Level.INFO, "SHAvite512 : {0}", new Object[]{Hex.encodeHexString(hash)});
        hash = new fr.cryptohash.SIMD512().digest(hash);
        //  LOG.log(Level.INFO, "SIMD512 : {0}", new Object[]{Hex.encodeHexString(hash)});
        hash = new fr.cryptohash.ECHO512().digest(hash);
        //  LOG.log(Level.INFO, "ECHO512 : {0}", new Object[]{Hex.encodeHexString(hash)});
        byte[] resultHash = new byte[32];
        System.arraycopy(hash, 0, resultHash, 0, 32);

        return resultHash;
    }
}
