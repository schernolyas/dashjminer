/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.cryptopool;

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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import ru.schernolyas.stratum.client.minimg.MimingRecursiveTask;

/**
 * Pools for crypto algorithms
 *
 * @author schernolyas
 */
public class CryptoPool {

    private static final Logger LOG = Logger.getLogger(CryptoPool.class.getName());

    private final static GenericObjectPoolConfig POOL_CONFIG;
    private final static GenericObjectPool<BLAKE512> BLAKE512_POOL;
    private final static GenericObjectPool<BMW512> BMW512_POOL;
    private final static GenericObjectPool<Groestl512> GROESTL512_POOL;
    private final static GenericObjectPool<Skein512> SKEIN512_POOL;
    private final static GenericObjectPool<JH512> JH512_POOL;
    private final static GenericObjectPool<Keccak512> KECCAK512_POOL;
    private final static GenericObjectPool<Luffa512> LUFFA512_POOL;
    private final static GenericObjectPool<CubeHash512> CUBE_HASH_512_POOL;
    private final static GenericObjectPool<SHAvite512> SHA_VITE_512_POOL;
    private final static GenericObjectPool<SIMD512> SIMD_512_POOL;
    private final static GenericObjectPool<ECHO512> ECHO_512_POOL;

    static {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(MimingRecursiveTask.GROUP_SIZE + 3);
        POOL_CONFIG = config;
        BLAKE512_POOL = new GenericObjectPool<>(new Blake512Factory(), POOL_CONFIG);
        BMW512_POOL = new GenericObjectPool<>(new BMW512Factory(), POOL_CONFIG);
        GROESTL512_POOL = new GenericObjectPool<>(new Groestl512Factory(), POOL_CONFIG);
        SKEIN512_POOL = new GenericObjectPool<>(new Skein512Factory(), POOL_CONFIG);
        JH512_POOL = new GenericObjectPool<>(new JH512Factory(), POOL_CONFIG);
        KECCAK512_POOL = new GenericObjectPool<>(new Keccak512Factory(), POOL_CONFIG);
        LUFFA512_POOL = new GenericObjectPool<>(new Luffa512Factory(), POOL_CONFIG);
        CUBE_HASH_512_POOL = new GenericObjectPool<>(new CubeHash512Factory(), POOL_CONFIG);
        SHA_VITE_512_POOL = new GenericObjectPool<>(new SHAvite512Factory(),POOL_CONFIG);
        SIMD_512_POOL = new GenericObjectPool<>(new Simd512Factory(),POOL_CONFIG);
        ECHO_512_POOL = new GenericObjectPool<>(new Echo512Factory(),POOL_CONFIG);

        LOG.log(Level.INFO, "pools created");

    }

    public static ObjectPool<BLAKE512> getBlake512Pool() {
        return BLAKE512_POOL;
    }

    public static ObjectPool<BMW512> getBMW512Pool() {
        return BMW512_POOL;
    }

    public static ObjectPool<Groestl512> getGroestl512Pool() {
        return GROESTL512_POOL;
    }

    public static ObjectPool<Skein512> getSkein512Pool() {
        return SKEIN512_POOL;
    }

    public static ObjectPool<JH512> getJh512Pool() {
        return JH512_POOL;
    }
    
    public static ObjectPool<Keccak512> getKeccak512Pool() {
        return KECCAK512_POOL;
    }
    
    public static ObjectPool<Luffa512> getLuffa512Pool() {
        return LUFFA512_POOL;
    }
    
    public static ObjectPool<CubeHash512> getCubeHash512Pool() {
        return CUBE_HASH_512_POOL;
    }
    
    public static ObjectPool<SHAvite512> getSHAvite512Pool() {
        return SHA_VITE_512_POOL;
    }
    
    public static ObjectPool<SIMD512> getSimd512Pool() {
        return SIMD_512_POOL;
    }
    
    public static ObjectPool<ECHO512> getEcho512Pool() {
        return ECHO_512_POOL;
    }

}
