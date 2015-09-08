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
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.schernolyas.stratum.client.utils.ByteUtils;
import ru.schernolyas.stratum.client.utils.NonceTimeHolder;

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
        byte[] blockHeaderTemplate = Hex.decodeHex(("03000000"
                + "b41a9bb47033147cd5c191eda450437c61ed3f92106913070001df4e00000000"
                + "d9ddb44caa7e944be785b7e25fc59e410861a5d14a53e6fbe87907fe78abef180"
                + "00000008c661c1b00000000").toCharArray());
        
        System.out.println("example nonce:"+Long.toHexString(2065871376L));
        System.out.println("example time:"+Long.toHexString(1441460065L));
        final byte[] time = Hex.decodeHex("53bf3cd7".toCharArray());         
        final byte[] startNonce = Hex.decodeHex("FEDCBA98".toCharArray());
        BlockHeaderCandidateProducer instance = new BlockHeaderCandidateProducer(blockHeaderTemplate, new NonceTimeHolder() {

            @Override
            public byte[] getNTime() {
                return time;
            }

            @Override
            public byte[] getNonce() {
                return startNonce;
            }

        });
        byte[] actualResult = instance.produceBlockHeaderCandidate();
        System.out.println(Hex.encodeHexString(actualResult));
        byte[] actualNonce = new byte[4];
        System.arraycopy(actualResult, (4+32+32+4+4), actualNonce, 0, 4);
        byte[] actualTime = new byte[4];
        System.arraycopy(actualResult, (4+32+32), actualTime, 0, 4);
        assertArrayEquals(ByteUtils.littleEndian(time), actualTime);
        assertArrayEquals(ByteUtils.littleEndian(startNonce), actualNonce);   
        assertEquals(actualResult.length,80L);
    }

}
