/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.utils;

import java.io.IOException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Сергей
 */
public class ByteUtilsTest {
    
    public ByteUtilsTest() {
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
     * Test of littleEndian method, of class ByteUtils.
     */
    @Test
    public void testLittleEndian() throws DecoderException {
        System.out.println("littleEndian");
        byte[] in = Hex.decodeHex("1234".toCharArray());
        ByteUtils instance = new ByteUtils();
        byte[] expResult = Hex.decodeHex("3412".toCharArray());
        byte[] result = instance.littleEndian(in);
        System.out.println("result :"+Hex.encodeHexString(result));
        assertArrayEquals(expResult, result);
    }
    /**
     * Test of littleEndian method, of class ByteUtils.
     */
    @Test
    public void testSwapOrder() throws DecoderException, IOException {
        System.out.println("swapOrder");
        byte[] in = Hex.decodeHex("1234".toCharArray());
        ByteUtils instance = new ByteUtils();
        byte[] expResult = Hex.decodeHex("4321".toCharArray());
        byte[] result = instance.swapOrder(in);
        System.out.println("result :"+Hex.encodeHexString(result));
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of preparePrevHash method, of class ByteUtils.
     */
   /* @Test
    public void testPreparePrevHash() throws Exception {
        System.out.println("preparePrevHash");
        byte[] prevHashButes = null;
        ByteUtils instance = new ByteUtils();
        byte[] expResult = null;
        byte[] result = instance.preparePrevHash(prevHashButes);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    } */
    
}
