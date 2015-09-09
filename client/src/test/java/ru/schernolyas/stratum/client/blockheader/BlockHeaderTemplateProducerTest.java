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

        Initial initial = Initial.build("{\"id\":1,\"result\":[[[\"mining.set_difficulty\",\"deadbeefcafebabe818f0f0000000000\"],[\"mining.notify\",\"deadbeefcafebabe818f0f0000000000\"]],\"7ff83614\",4],\"error\":null}");
        MiningNotify miningNotify = MiningNotify.build("{\"id\":null,\"method\":\"mining.notify\",\"params\":[\"501d\","
                + "\"b41a9bb47033147cd5c191eda450437c61ed3f92106913070001df4e00000000\",\"01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff20037814050450dcee5508\","
                + "\"0d2f6e6f64655374726174756d2f00000000023d7c6f11000000001976a914edc3ed0229526ea4c728d714b5289f97e7f63a2188ac377c6f11000000001976a9142491bea15e2e948e78230c5d6d6754a4fc4cd1a288ac00000000\","
                + "[\"08166f0b04c138d45498d7fc70a4dee2f15afd5a24b36a94f02542d61c8c33ec\",\"16ef35eed1539b0c1dacbaf130a949a2e1e0f67e4b1245823c8cb1e192d44945\",\"d823c777769a6e3db63b4c7a563f26554585f71ab7e0f24acd6c01c03dd82db5\"],"
                + "\"00000003\",\"1b1c668c\",\"55eedc6c\",false]}");
        System.out.println("BlockVersion: " + Hex.encodeHexString(miningNotify.getBlockVersion()));
        //todo: may be incorrect!!!
        String directFinalMerkleRoot = "d9ddb44caa7e944be785b7e25fc59e410861a5d14a53e6fbe87907fe78abef18";
        BlockHeaderTemplateProducer instance = new BlockHeaderTemplateProducer(miningNotify, initial);
        byte[] result = instance.produceBlockHeaderTemplate();
        byte[] directNBits = miningNotify.getEncodedNetworkDifficulty();
        byte[] resultNBits = new byte[4];
        byte[] resultVersion = new byte[4];
        byte[] resultPrevBlockHash = new byte[32];
        byte[] resultMerkleRoot = new byte[32];
        System.arraycopy(result, (4 + 32 + 32 + 4), resultNBits, 0, 4);
        System.arraycopy(result, 0, resultVersion, 0, 4);
        System.arraycopy(result, 4, resultPrevBlockHash, 0, 32);
        System.arraycopy(result, 4 + 32, resultMerkleRoot, 0, 32);
        System.out.println("result: " + Hex.encodeHexString(result));
        System.out.println("direct nBits: " + Hex.encodeHexString(miningNotify.getEncodedNetworkDifficulty()));
        System.out.println("direct nBits: " + Hex.encodeHexString(directNBits));
        assertArrayEquals(directNBits, resultNBits);
        assertArrayEquals(ByteUtils.littleEndian(miningNotify.getBlockVersion()), resultVersion);
//        assertArrayEquals(miningNotify.getPreviousBlockHash(), resultPrevBlockHash);
        //assertArrayEquals(Hex.decodeHex(directFinalMerkleRoot.toCharArray()), resultMerkleRoot);
        //  real example
        //getblockheader 000000000013d72fca80766d7f5c362ddb3ed0bcbcba77b9cac8d01ee3a58b18 
        Initial initialMock = new Initial();
        MiningNotify miningNotifyMock = new MiningNotify();
        miningNotifyMock.setBlockVersion(new byte[]{0,0,0,3});
        miningNotifyMock.setEncodedNetworkDifficulty(Hex.decodeHex("5e3f1a1b".toCharArray()));
        miningNotifyMock.setPreviousBlockHash(Hex.decodeHex("9f224030db8309b38b755facdbeb5047c29fc963c31e1a9138d4020000000000".toCharArray()));
        
        BlockHeaderTemplateProducer instanceMock = new BlockHeaderTemplateProducer(miningNotifyMock, initialMock) {

            @Override
            protected byte[] calculateMerkelRoot() throws NoSuchAlgorithmException, IOException {
                byte[] bytes = null;
                try {
                    bytes = Hex.decodeHex("da5aa8c3ba44284fee52e1c5c65392af8a483e7427de3b58c3639cdc23265ea0".toCharArray());                   
                } catch (DecoderException e) {
                    e.printStackTrace();
                }
                return bytes;
            }

        };
        byte[] blockTemplate1 = instanceMock.produceBlockHeaderTemplate();
        //add required nonce and time
        Long requiredNonce = 2065871376L;
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES).putLong(requiredNonce);
        final byte[] requiredNonceBytes = get4Bytes(buffer.array());
        Long requiredTime = 1441460065L;
        buffer = ByteBuffer.allocate(Long.BYTES).putLong(requiredTime);
        final byte[] requiredTimeBytes =get4Bytes(buffer.array());
        
        System.out.println("blockTemplate1 : "+Hex.encodeHexString(blockTemplate1));
        System.out.println("requiredNonceBytes : "+Hex.encodeHexString(requiredNonceBytes));
        System.out.println("requiredTimeBytes : "+Hex.encodeHexString(requiredTimeBytes));
        byte[] blockCandidate = blockTemplate1;
        System.arraycopy(ByteUtils.littleEndian(requiredTimeBytes), 0, blockCandidate, TIME_POSITION, 4);
        System.arraycopy(ByteUtils.littleEndian(requiredNonceBytes), 0, blockCandidate, NONCE_POSITION, 4);        
        System.out.println("blockCandidate1 : "+Hex.encodeHexString(blockCandidate));
        byte[] x11Bytes = X11Util.calculate(blockCandidate);
        System.out.println("x11 bytes : "+Hex.encodeHexString(x11Bytes));
        byte[] consoleBlockHeader = Hex.decodeHex(("03000000"
                + "9f224030db8309b38b755facdbeb5047c29fc963c31e1a9138d4020000000000"
                + "da5aa8c3ba44284fee52e1c5c65392af8a483e7427de3b58c3639cdc23265ea0"
                + "61efea555e3f1a1b10b2227b").toCharArray());
        System.out.println("consoleBlockHeader : "+Hex.encodeHexString(consoleBlockHeader));
        assertArrayEquals(consoleBlockHeader, blockCandidate);
        System.out.println("console x11 bytes : "+Hex.encodeHexString(X11Util.calculate(consoleBlockHeader)));
        BlockHeaderCandidateProducer producer = new BlockHeaderCandidateProducer(blockTemplate1, new NonceTimeHolder() {

            @Override
            public byte[] getNTime() {
                return requiredTimeBytes;
            }

            @Override
            public byte[] getNonce() {
                return requiredNonceBytes;
            }
        });
        System.out.println("blockCandidate from producer : "+Hex.encodeHexString(producer.produceBlockHeaderCandidate()));
        assertArrayEquals(consoleBlockHeader, producer.produceBlockHeaderCandidate());
        System.out.println("producer x11 bytes : "+Hex.encodeHexString(X11Util.calculate(producer.produceBlockHeaderCandidate())));
        

    }
    
    private static byte[] get4Bytes(byte[] b) {
        byte[] b1 = new byte[4];
        System.arraycopy(b, 4, b1, 0, 4);
        return b1;
    }

}
