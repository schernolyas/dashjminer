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

    @Test
    public void testCalculateTargetByDifficulty() throws Exception {
        System.out.println("calculateTarget");
        BigDecimal decimalDifficulty = new BigDecimal("0.03125");
        byte[] expectedTarget = Hex.decodeHex(("0000000000000000000000000000000000000000000000000000e0ff1f000000").toCharArray());
        byte[] result = DifficultyUtil.calculateTargetByDifficulty(decimalDifficulty);
        assertArrayEquals(expectedTarget, result);
    }

}
