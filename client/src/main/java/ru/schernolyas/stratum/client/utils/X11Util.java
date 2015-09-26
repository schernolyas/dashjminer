/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.utils;

import fr.cryptohash.BLAKE512;
import fr.cryptohash.BMW512;
import fr.cryptohash.CubeHash512;
import fr.cryptohash.ECHO512;
import fr.cryptohash.Groestl512;
import fr.cryptohash.JH512;
import fr.cryptohash.Keccak512;
import fr.cryptohash.Luffa512;
import fr.cryptohash.SHAvite512;
import fr.cryptohash.SIMD512;
import fr.cryptohash.Skein512;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.logging.Logger;
import org.apache.commons.pool2.ObjectPool;
import ru.schernolyas.stratum.client.cryptopool.CryptoPool;

/**
 *
 * @author Сергей
 */
public class X11Util {

    private static final Logger LOG = Logger.getLogger(X11Util.class.getName());

    /**
     * <code>
     *
     *   pdata - currect block header
     *   <code>
     *   int kk=0;
     *	for (; kk < 32; kk++)
     *	{
     *		be32enc(&endiandata[kk], ((uint32_t*)pdata)[kk]);
     *	};
     *
     *       </code>
     *
     * </code>
     *
     * @param preparedX11BlockHeaderCandidate
     * @return
     * @throws Exception
     */
    public static byte[] calculate(byte[] preparedX11BlockHeaderCandidate) throws Exception {
         
        ObjectPool<BLAKE512> blake512Pool = CryptoPool.getBlake512Pool();
        BLAKE512 blake512 = blake512Pool.borrowObject();
        byte[] hash = blake512.digest(preparedX11BlockHeaderCandidate);
        blake512Pool.returnObject(blake512);

        ObjectPool<BMW512> bmw512Pool = CryptoPool.getBMW512Pool();
        BMW512 bmv512 = bmw512Pool.borrowObject();
        hash = bmv512.digest(hash);
        bmw512Pool.returnObject(bmv512);

        ObjectPool<Groestl512> groestl512Pool = CryptoPool.getGroestl512Pool();
        Groestl512 groestl512 = groestl512Pool.borrowObject();
        hash = groestl512.digest(hash);
        groestl512Pool.returnObject(groestl512);

        ObjectPool<Skein512> skein512Pool = CryptoPool.getSkein512Pool();
        Skein512 skein512 = skein512Pool.borrowObject();
        hash = skein512.digest(hash);
        skein512Pool.returnObject(skein512);

        
        ObjectPool<JH512> jh512Pool = CryptoPool.getJh512Pool();
        JH512 jh512 = jh512Pool.borrowObject();
        hash = jh512.digest(hash);
        jh512Pool.returnObject(jh512);

        ObjectPool<Keccak512> keccak512Pool = CryptoPool.getKeccak512Pool();
        Keccak512 keccak512 = keccak512Pool.borrowObject();
        hash = keccak512.digest(hash);
        keccak512Pool.returnObject(keccak512);

        ObjectPool<Luffa512> luffa512Pool = CryptoPool.getLuffa512Pool();
        Luffa512 luffa512 = luffa512Pool.borrowObject();
        hash = luffa512.digest(hash);
        luffa512Pool.returnObject(luffa512);

        ObjectPool<CubeHash512> cubeHash512Pool = CryptoPool.getCubeHash512Pool();
        CubeHash512 cubeHash512 = cubeHash512Pool.borrowObject();
        hash = cubeHash512.digest(hash);
        cubeHash512Pool.returnObject(cubeHash512);

        ObjectPool<SHAvite512> shavite512Pool = CryptoPool.getSHAvite512Pool();
        SHAvite512 shavite512 = shavite512Pool.borrowObject();
        hash = shavite512.digest(hash);
        shavite512Pool.returnObject(shavite512);

        ObjectPool<SIMD512> simd512Pool = CryptoPool.getSimd512Pool();
        SIMD512 simd512 = simd512Pool.borrowObject();
        hash = simd512.digest(hash);
        simd512Pool.returnObject(simd512);

        ObjectPool<ECHO512> echo512Pool = CryptoPool.getEcho512Pool();
        ECHO512 echo512 = echo512Pool.borrowObject();
        hash = echo512.digest(hash);
        echo512Pool.returnObject(echo512);
        return Arrays.copyOfRange(hash,31, 63) ;
    }
}
