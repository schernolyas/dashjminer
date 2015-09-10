/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.binary.Hex;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import ru.schernolyas.stratum.client.method.SetDifficulty;

/**
 *
 * @author schernolyas
 */
public class DifficultyUtilTest {

    public DifficultyUtilTest() {
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
     * Test of calculateTarget method, of class DifficultyUtil.
     */
    @Test
    public void testCalculateTarget_BigDecimal() throws Exception {
        System.out.println("calculateTarget");
        BigDecimal decimalDifficulty = new BigDecimal("0.03125");

        byte[] result = DifficultyUtil.calculateTarget(decimalDifficulty);
        //assertArrayEquals(expResult, result);
        System.out.println(Hex.encodeHexString(result));

        decimalDifficulty = new BigDecimal("0.12");
        String h = Float.toHexString(decimalDifficulty.floatValue());
        Pattern p = Pattern.compile("0x1\\.(.*)p(.*)");
        Matcher matcher = p.matcher(h);
        if (matcher.find()) {
            System.out.println("group1: " + matcher.group(1) + ";" + matcher.group(2));
        }
        System.out.println("!" + Float.toHexString(decimalDifficulty.floatValue()));
        result = DifficultyUtil.calculateTarget(decimalDifficulty);
        //assertArrayEquals(expResult, result);
        System.out.println(Hex.encodeHexString(result));
        
        //calculate https://bitcoin.org/en/developer-reference#target-nbits
        String stratumNBits = "1b18679a";
        String mantissa = "18679a";
        int mantissaByteCount = 3;
        int exponent = 0x1b;
        BigInteger target = new BigInteger(mantissa,16).multiply(new BigInteger("256",10).pow(exponent-mantissaByteCount));
        System.out.println("target: "+target.toString(16));
        
        
    }

}
