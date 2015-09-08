/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.blockheader;

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
     * Test of produceBlockHeaderTemplate method, of class BlockHeaderTemplateProducer.
     */
    @Test
    public void testProduceBlockHeaderTemplate() throws Exception {
        System.out.println("produceBlockHeaderTemplate");
        
        Long eNonce = 2134052L;
        System.out.println("eNonce; "+Long.toHexString(eNonce));
        
        
        Initial initial = Initial.build("{\"id\":1,\"result\":[[[\"mining.set_difficulty\",\"deadbeefcafebabe818f0f0000000000\"],[\"mining.notify\",\"deadbeefcafebabe818f0f0000000000\"]],\"7ff83614\",4],\"error\":null}");
        MiningNotify miningNotify = MiningNotify.build("{\"id\":null,\"method\":\"mining.notify\",\"params\":[\"501d\","
                + "\"b41a9bb47033147cd5c191eda450437c61ed3f92106913070001df4e00000000\",\"01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff20037814050450dcee5508\","
                + "\"0d2f6e6f64655374726174756d2f00000000023d7c6f11000000001976a914edc3ed0229526ea4c728d714b5289f97e7f63a2188ac377c6f11000000001976a9142491bea15e2e948e78230c5d6d6754a4fc4cd1a288ac00000000\","
                + "[\"08166f0b04c138d45498d7fc70a4dee2f15afd5a24b36a94f02542d61c8c33ec\",\"16ef35eed1539b0c1dacbaf130a949a2e1e0f67e4b1245823c8cb1e192d44945\",\"d823c777769a6e3db63b4c7a563f26554585f71ab7e0f24acd6c01c03dd82db5\"],"
                + "\"00000003\",\"1b1c668c\",\"55eedc6c\",false]}");
        System.out.println("BlockVersion: "+Hex.encodeHexString(miningNotify.getBlockVersion()));
        BlockHeaderTemplateProducer instance = new BlockHeaderTemplateProducer(miningNotify, initial);
        byte[] result = instance.produceBlockHeaderTemplate();
        System.out.println("result: "+Hex.encodeHexString(result));
        
    }
    
}
