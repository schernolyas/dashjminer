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
     * hex (5628506) -> 55E25A
     * little-endian hex (5628506) -> 5ae25500
     * Test of littleEndian method, of class ByteUtils.
     * @throws org.apache.commons.codec.DecoderException
     */
    @Test
    public void testLittleEndian() throws DecoderException {
        System.out.println("littleEndian");
        byte[] in = Hex.decodeHex("01234567".toCharArray());
        byte[] expResult = Hex.decodeHex("67452301".toCharArray());
        byte[] result = ByteUtils.littleEndian(in);
        System.out.println("result :"+Hex.encodeHexString(result));
        assertArrayEquals(expResult, result);
    }
    
    @Test
    public void testFastCompare() throws IOException {
        System.out.println("fastCompare");
        byte[] array1 = new byte[]{0,0,0,2,1};
        byte[] array2 = new byte[]{0,0,0,0,9};
        assertEquals(1L, ByteUtils.fastCompare(array1, array2));
        array2 = new byte[]{0,0,0,2,1};
        array1 = new byte[]{0,0,0,0,9};
        assertEquals(-1L, ByteUtils.fastCompare(array1, array2));
    }
   
    @Test
    public void testSwapOrder() throws DecoderException, IOException {
        System.out.println("swapOrder");
        byte[] in = Hex.decodeHex("1234".toCharArray());
        byte[] expResult = Hex.decodeHex("4321".toCharArray());
        byte[] result = ByteUtils.swapOrder(in);
        System.out.println("result :"+Hex.encodeHexString(result));
        assertArrayEquals(expResult, result);
    }
    
    /**
     * Test of concat method, of class ByteUtils.
     * @throws java.io.IOException
     */
    @Test
    public void testConcat() throws IOException {
        System.out.println("concat");
        byte[][] in = new byte[2][];
        in[0]=new byte[]{1,2};
        in[1]=new byte[]{3,4};
        byte[] expResult = new byte[]{1,2,3,4};
        byte[] result = ByteUtils.concat(in);
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
