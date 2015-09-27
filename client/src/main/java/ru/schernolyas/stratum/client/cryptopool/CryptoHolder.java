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

/**
 *
 * @author Sergey Chernolyas
 */
public class CryptoHolder {
    private final BMW512 bmw512 = new BMW512();
    private final BLAKE512 blake512 = new BLAKE512();
    private final CubeHash512 cubeHash512 = new CubeHash512();
    private final ECHO512 echo512 = new ECHO512();
    private final Groestl512 groestl512 = new Groestl512();
    private final JH512 jh512 = new JH512();
    private final Keccak512 keccak512 = new Keccak512();
    private final Luffa512 luffa512 = new Luffa512();
    private final SHAvite512 shaVite512 = new SHAvite512();
    private final SIMD512 simd512 = new SIMD512();
    private final Skein512 skein512 = new Skein512();

    public BMW512 getBmw512() {
        return bmw512;
    }

    public BLAKE512 getBlake512() {
        return blake512;
    }

    public CubeHash512 getCubeHash512() {
        return cubeHash512;
    }

    public ECHO512 getEcho512() {
        return echo512;
    }

    public Groestl512 getGroestl512() {
        return groestl512;
    }

    public JH512 getJh512() {
        return jh512;
    }

    public Keccak512 getKeccak512() {
        return keccak512;
    }

    public Luffa512 getLuffa512() {
        return luffa512;
    }

    public SHAvite512 getShaVite512() {
        return shaVite512;
    }

    public SIMD512 getSimd512() {
        return simd512;
    }

    public Skein512 getSkein512() {
        return skein512;
    }
    
    
    
}
