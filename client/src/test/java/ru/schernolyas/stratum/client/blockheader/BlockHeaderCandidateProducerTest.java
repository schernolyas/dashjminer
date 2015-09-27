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
import static org.junit.Assert.assertArrayEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.schernolyas.stratum.client.utils.NonceTimeHolderImpl;

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
     * Test of produceBlockHeaderCandidate method, of class
     * BlockHeaderCandidateProducer.
     *
     * @throws org.apache.commons.codec.DecoderException
     */
    @Test
    public void testProduceBlockHeader() throws DecoderException {
        System.out.println("produceBlockHeader");
        byte[] blockHeaderTemplate = Hex.decodeHex(("00000003"
                + "7fb4163f834ae0187184bd1ae35df007fb61ddf0c6f9152d0012b85200000000"
                + "4568febd9362ebfa0a9e10a1092e44f9a89a09921e66d264531b5b9481fda193"
                + "5605647e"
                + "1b195308"
                + "00000000").toCharArray());
        byte[] expectedBlockHeaderTemplate1 = Hex.decodeHex(("00000003"
                + "7fb4163f834ae0187184bd1ae35df007fb61ddf0c6f9152d0012b85200000000"
                + "4568febd9362ebfa0a9e10a1092e44f9a89a09921e66d264531b5b9481fda193"
                + "5605647e"
                + "1b195308"
                + "00000000").toCharArray());
        byte[] expectedBlockHeaderTemplate2 = Hex.decodeHex(("00000003"
                + "7fb4163f834ae0187184bd1ae35df007fb61ddf0c6f9152d0012b85200000000"
                + "4568febd9362ebfa0a9e10a1092e44f9a89a09921e66d264531b5b9481fda193"
                + "5605647e"
                + "1b195308"
                + "01000000").toCharArray());
        
        
        BlockHeaderCandidateProducer instance = new BlockHeaderCandidateProducer(blockHeaderTemplate, new NonceTimeHolderImpl());
        byte[] actualResult1 = instance.produceBlockHeaderCandidate();
        System.out.println("actual Block Header Candidate 1:"+Hex.encodeHexString(actualResult1));
        assertArrayEquals(expectedBlockHeaderTemplate1, actualResult1);
        
        byte[] actualResult2 = instance.produceBlockHeaderCandidate();
        System.out.println("actual Block Header Candidate 2:"+Hex.encodeHexString(actualResult2));
        assertArrayEquals(expectedBlockHeaderTemplate2, actualResult2);
    }

}
