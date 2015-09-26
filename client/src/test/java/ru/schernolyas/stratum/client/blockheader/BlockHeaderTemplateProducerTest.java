/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.blockheader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static ru.schernolyas.stratum.client.blockheader.BlockHeaderCandidateProducer.NONCE_POSITION;
import static ru.schernolyas.stratum.client.blockheader.BlockHeaderCandidateProducer.TIME_POSITION;
import ru.schernolyas.stratum.client.method.Initial;
import ru.schernolyas.stratum.client.method.MiningNotify;
import ru.schernolyas.stratum.client.utils.ByteUtils;
import ru.schernolyas.stratum.client.utils.NonceTimeHolder;
import ru.schernolyas.stratum.client.utils.X11Util;

/**
 *
 * @author schernolyas
 */
public class BlockHeaderTemplateProducerTest {

    public BlockHeaderTemplateProducerTest() {
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
     * Test of produceBlockHeaderTemplate method, of class
     * BlockHeaderTemplateProducer.
     */
    @Test
    public void testProduceBlockHeaderTemplate() throws Exception {
        System.out.println("produceBlockHeaderTemplate");

        Long eNonce = 2065871376L;
        System.out.println("eNonce: " + Long.toHexString(eNonce));

        Initial initial = Initial.build("{\"id\":1,\"result\":[[[\"mining.set_difficulty\",\"deadbeefcafebabe818f0f0000000000\"],[\"mining.notify\",\"deadbeefcafebabe818f0f0000000000\"]],\"7fedb6ae\",4],\"error\":null}");
        MiningNotify miningNotify = MiningNotify.build("{\"id\":null,\"method\":\"mining.notify\",\"params\":[\"501d\","
                + "\"7fb4163f834ae0187184bd1ae35df007fb61ddf0c6f9152d0012b85200000000\",\"01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff2003133905047e64055608\","
                + "\"0d2f6e6f64655374726174756d2f0000000002f3da000f000000001976a914edc3ed0229526ea4c728d714b5289f97e7f63a2188aceada000f000000001976a914379ec5e42845cf0c93294952a8e9e55047bb0ca988ac00000000\","
                + "[\"fba3f6bf08fbabafdf3ae1d66dd13a8798eaf29a39235d495d1bcd02ef2e8f37\",\"7f218cf6fc3223d497c5ed4b72b7c6df3e8d679b93dc34932978774b011a9cc8\",\"a211087f67f08362489fbd44fc0375c76e80c7e6f7321fa3c9885e09bafec1d3\"],"
                + "\"00000003\",\"1b195308\",\"5605647e\",false]}");
        System.out.println("BlockVersion: " + Hex.encodeHexString(miningNotify.getBlockVersion()));
       
        
        BlockHeaderTemplateProducer instance = new BlockHeaderTemplateProducer(miningNotify, initial);
        byte[] actualBlockHeaderTemplate = instance.produceBlockHeaderTemplate();
        byte[] expectedBlockHeaderTemplate = Hex.decodeHex(("00000003"
                + "7fb4163f834ae0187184bd1ae35df007fb61ddf0c6f9152d0012b85200000000"
                + "4568febd9362ebfa0a9e10a1092e44f9a89a09921e66d264531b5b9481fda1935"
                + "605647e1b19530800000000").toCharArray());
        System.out.println("expectedBlockHeaderTemplate: " + Hex.encodeHexString(expectedBlockHeaderTemplate));
        System.out.println("actualBlockHeaderTemplate  : " + Hex.encodeHexString(actualBlockHeaderTemplate));
        assertArrayEquals(expectedBlockHeaderTemplate, actualBlockHeaderTemplate);
    }
    
    @Test
    public void testCalculateMerkelRoot() throws Exception {
        System.out.println("calculateMerkelRoot");
        MiningNotify miningNotify = new MiningNotify();
        miningNotify.setCoinBase1(Hex.decodeHex(("01000000010000000000000000000000000000000000000000000000000000000000000000"
                + "ffffffff2003833205040457015608").toCharArray()));
        miningNotify.setCoinBase2(Hex.decodeHex(("0d2f6e6f64655374726174756d2f0000000002f3da000f000000001976a914edc3ed0229526ea4c728d714b5289f97e7f63a2188aceada000f"
                + "000000001976a9146454ca08ba97746055ffaf2023dbfd241708fb7c88ac00000000").toCharArray()));
        byte[][] merkleBranches = new byte[3][];
        merkleBranches[0] = Hex.decodeHex("b7d74cc1c8a608d5d56c2fbdf687d7285fa22eee8525008e7768341630bfa0c3".toCharArray());
        merkleBranches[1] = Hex.decodeHex("81bf5e828cf5a2feac1b37140d97ebbff9d30f01c26b8416fd2dfbd0934e3934".toCharArray());
        merkleBranches[2] = Hex.decodeHex("5506686705125068251ca9e2814c39be64ad3869ab8579d7fc0cdf35ce7f6362".toCharArray());
        miningNotify.setMerkleBranches(merkleBranches);
        Initial initial = new Initial();
        initial.setExtraNonce2Size(4);
        initial.setExtraNonce1(Hex.decodeHex("7fefc86c".toCharArray()));
        
        BlockHeaderTemplateProducer instance = new BlockHeaderTemplateProducer(miningNotify, initial);
        byte[] expectedMerkleRoot = Hex.decodeHex(("08a8ae703d05d04518b07ffa6b70dbe88dcad8451681b05155a8116bbcab1772").toCharArray());
        byte[] actualMerkleRoot = instance.calculateMerkelRoot();
        assertArrayEquals(expectedMerkleRoot,actualMerkleRoot);
    }
    
    

}
