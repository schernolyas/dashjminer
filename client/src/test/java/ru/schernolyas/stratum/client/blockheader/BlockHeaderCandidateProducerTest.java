/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.blockheader;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import ru.schernolyas.stratum.client.utils.NonceTimeUtil;

/**
 *
 * @author schernolyas
 */
public class BlockHeaderCandidateProducerTest {
    
    public BlockHeaderCandidateProducerTest() {
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
     * Test of produceBlockHeaderCandidate method, of class BlockHeaderCandidateProducer.
     * @throws org.apache.commons.codec.DecoderException
     */
    @Test
    public void testProduceBlockHeader() throws DecoderException {
        System.out.println("produceBlockHeader");
        byte[] blockHeaderTemplate = Hex.decodeHex(("02000000c38a597df65407ee4ef4450774ecb670c95dab99ecf431924b8d100000000000816e35bd1f"
                + "2381413da068454fd51151de2e6bab4d72b3279dec857a5e30eb430000000042cf101b00000000").toCharArray());
        byte[] expectedResult = Hex.decodeHex(("02000000c38a597df65407ee4ef4450774ecb670c95dab99ecf431924b8d100000000000816e35bd1f2381413da068"
                + "454fd51151de2e6bab4d72b3279dec857a5e30eb43d73cbf5342cf101bfabadcfe").toCharArray());
        
        byte[] time = Hex.decodeHex("53bf3cd7".toCharArray());
        long startNonce = 0xFEDCBAFA;
        NonceTimeUtil nonceTimeUtil = new NonceTimeUtil(time, startNonce);
        BlockHeaderCandidateProducer instance = new BlockHeaderCandidateProducer(blockHeaderTemplate, nonceTimeUtil);
        byte[] actualResult = instance.produceBlockHeaderCandidate();
        System.out.println(Hex.encodeHexString(actualResult));
        assertArrayEquals(expectedResult, actualResult);
        assertEquals(expectedResult.length, 80L);        
    }
    
}
