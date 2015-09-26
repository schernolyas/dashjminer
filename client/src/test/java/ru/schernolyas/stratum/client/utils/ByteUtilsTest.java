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
    public void testFastCompare() throws IOException, DecoderException {
        System.out.println("fastCompare");
        byte[] array1 = new byte[]{0,0,0,2,1};
        byte[] array2 = new byte[]{0,0,0,0,9};
        //assertEquals(1L, ByteUtils.fastCompare(array1, array2));        
        array1 = new byte[]{0,0,0,0,9};
        array2 = new byte[]{0,0,0,2,1};
       // assertEquals(-1L, ByteUtils.fastCompare(array1, array2));
        System.out.println("real example");
        byte[] littleEndianX11Hash = Hex.decodeHex("f6121406354ecb3a73d1a8f996c041c5d8a266a5fb0e8f2e9791873a394468a9".toCharArray());
        byte[] target = Hex.decodeHex("0000000000240481000000000000000000000000000000000000000000000000".toCharArray());
        assertEquals(-1L, ByteUtils.fastCompare(target, littleEndianX11Hash));
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
    @Test
    public void testPreparePrevHash() throws Exception {
        System.out.println("preparePrevHash");
        byte[] expectedPrevHashBytes = Hex.decodeHex("4568febd9362ebfa0a9e10a1092e44f9a89a09921e66d264531b5b9481fda193".toCharArray());
        byte[] prevHashBytes = Hex.decodeHex("bdfe6845faeb6293a1109e0af9442e0992099aa864d2661e945b1b5393a1fd81".toCharArray());
        byte[] result = ByteUtils.preparePrevHash(prevHashBytes);
        assertArrayEquals(expectedPrevHashBytes, result);        
    } 
    @Test
    public void testPrepareForX11Hash() throws Exception {
        System.out.println("prepareForX11Hash");
        byte[] expectedPrevHashBytes = Hex.decodeHex(("03000000"
                + "29370132bb497a4d13c650774076f03471fb8669f1adeea276a20a0000000000"
                + "e9dbf74af1f9da0abd8642d2a0cde9fad4c7bef19e43304a8c1938bb55e1e967b164055621a81b1b"
                + "00000000").toCharArray());
        
        byte[] blockHeaderCandidateBytes = Hex.decodeHex(("00000003"
                + "320137294d7a49bb7750c61334f076406986fb71a2eeadf1000aa27600000000"
                + "4af7dbe90adaf9f1d24286bdfae9cda0f1bec7d44a30439ebb38198c67e9e155560564b11b1ba821"
                + "00000000").toCharArray());
        byte[] result = ByteUtils.prepareForX11Hash(blockHeaderCandidateBytes);
        assertArrayEquals(expectedPrevHashBytes, result);        
    }
    
}
