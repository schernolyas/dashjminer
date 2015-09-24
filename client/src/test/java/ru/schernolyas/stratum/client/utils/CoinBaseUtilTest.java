/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.utils;

import org.apache.commons.codec.binary.Hex;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import ru.schernolyas.stratum.client.method.Initial;
import ru.schernolyas.stratum.client.method.MiningNotify;

/**
 *
 * @author schernolyas
 */
public class CoinBaseUtilTest {
    
    public CoinBaseUtilTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of produceCoinBase method, of class CoinBaseUtil.
     */
    @Test
    public void testProduceCoinBase() throws Exception {
        System.out.println("produceCoinBase");
        MiningNotify miningNotify = new MiningNotify();
        miningNotify.setCoinBase1(Hex.decodeHex(("01000000010000000000000000000000000000000000000000000000000000000000000000"
                + "ffffffff2003833205040457015608").toCharArray()));
        miningNotify.setCoinBase2(Hex.decodeHex(("0d2f6e6f64655374726174756d2f0000000002f3da000f000000001976a914edc3ed0229526ea4c728d714b5289f97e7f63a2188aceada000f"
                + "000000001976a9146454ca08ba97746055ffaf2023dbfd241708fb7c88ac00000000").toCharArray()));
        Initial initial = new Initial();
        initial.setExtraNonce2Size(4);
        initial.setExtraNonce1(Hex.decodeHex("7fefc86c".toCharArray()));
        byte[] expResult = Hex.decodeHex(("01000000010000000000000000000000000000000000000000000000000000000000000000"
                + "ffffffff20038332050404570156087fefc86c000000000d2f6e6f64655374726174756d2f0000000002f3da000f000000001976a914edc3ed0229526ea4c728d714b5289f97"
                + "e7f63a2188aceada000f000000001976a9146454ca08ba97746055ffaf2023dbfd241708fb7c88ac00000000").toCharArray());
        byte[] result = CoinBaseUtil.produceCoinBase(miningNotify, initial);
        assertArrayEquals(expResult, result);        
    }
    
}
