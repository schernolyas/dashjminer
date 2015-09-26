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
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.schernolyas.stratum.client.method.Initial;
import ru.schernolyas.stratum.client.method.MiningNotify;
import ru.schernolyas.stratum.client.utils.ByteUtils;
import ru.schernolyas.stratum.client.utils.DifficultyUtil;
import ru.schernolyas.stratum.client.utils.NonceTimeHolder;
import ru.schernolyas.stratum.client.utils.X11Util;

/**
 *
 * @author schernolyas
 */

public class ComplexBlockHeaderTest {

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

    //@Test    
    public void testProduceBlockHeader() throws Exception {
        System.out.println("produceBlockHeader");

        final byte[] expectedBlockHeader = Hex.decodeHex(("03000000"
                + "9f224030db8309b38b755facdbeb5047c29fc963c31e1a9138d4020000000000"
                + "da5aa8c3ba44284fee52e1c5c65392af8a483e7427de3b58c3639cdc23265ea0"
                + "61efea555e3f1a1b10b2227b").toCharArray());
        System.out.println("expectedBlockHeader : " + Hex.encodeHexString(expectedBlockHeader));
        final byte[] expectedX11Bytes = ByteUtils.littleEndian(Hex.decodeHex(("000000000013d72fca80766d7f5c362ddb3ed0bcbcba77b9cac8d01ee3a58b18").toCharArray()));
        System.out.println("expectedBlockHeader : " + Hex.encodeHexString(expectedBlockHeader));
        byte[] expectedTarget = DifficultyUtil.calculateTarget(Hex.decodeHex("1b1a3f5e".toCharArray()));
        expectedTarget = ByteUtils.extend(expectedTarget, 32);
        System.out.println("expectedTarget : " + Hex.encodeHexString(expectedTarget));

        Initial initialMock = new Initial();
        MiningNotify miningNotifyMock = new MiningNotify();
        miningNotifyMock.setBlockVersion(new byte[]{0, 0, 0, 3});
        miningNotifyMock.setEncodedNetworkDifficulty(Hex.decodeHex("5e3f1a1b".toCharArray()));
        miningNotifyMock.setPreviousBlockHash(Hex.decodeHex("9f224030db8309b38b755facdbeb5047c29fc963c31e1a9138d4020000000000".toCharArray()));
        BlockHeaderTemplateProducer blockHeaderTemplateProducerMock = new BlockHeaderTemplateProducer(miningNotifyMock, initialMock) {

            @Override
            protected byte[] calculateMerkelRoot() throws NoSuchAlgorithmException, IOException {
                return prepareMerkelRoot();
            }
            
        };
       
        
        byte[] blockHeaderTemplate = blockHeaderTemplateProducerMock.produceBlockHeaderTemplate();
        //add required nonce and time
        Long requiredNonce = 2065871376L;
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES).putLong(requiredNonce);
        final byte[] requiredNonceBytes = get4Bytes(buffer.array());
        Long requiredTime = 1441460065L;
        buffer = ByteBuffer.allocate(Long.BYTES).putLong(requiredTime);
        final byte[] requiredTimeBytes = get4Bytes(buffer.array());

        System.out.println("blockHeaderTemplate : " + Hex.encodeHexString(blockHeaderTemplate));
        System.out.println("requiredNonceBytes : " + Hex.encodeHexString(requiredNonceBytes));
        System.out.println("requiredTimeBytes : " + Hex.encodeHexString(requiredTimeBytes));

        BlockHeaderCandidateProducer producer = new BlockHeaderCandidateProducer(blockHeaderTemplate, new NonceTimeHolder() {

            @Override
            public byte[] getNTime() {
                return requiredTimeBytes;
            }

            @Override
            public byte[] getNonce() {
                return requiredNonceBytes;
            }
        });

        byte[] actualBlockCandidate = producer.produceBlockHeaderCandidate();
        System.out.println("blockCandidate : " + Hex.encodeHexString(actualBlockCandidate));
        byte[] actualX11Bytes = X11Util.calculate(actualBlockCandidate);
        System.out.println("x11 bytes : " + Hex.encodeHexString(actualX11Bytes));

        assertArrayEquals(expectedBlockHeader, actualBlockCandidate);
        assertArrayEquals(expectedX11Bytes, actualX11Bytes);

        System.out.println("expected x11 bytes : " + Hex.encodeHexString(X11Util.calculate(expectedBlockHeader)));
        System.out.println("producer x11 bytes : " + Hex.encodeHexString(X11Util.calculate(actualBlockCandidate)));
        byte[] littleEndianActualX11Bytes = ByteUtils.littleEndian(actualX11Bytes);
        System.out.println("littleEndianActualX11Bytes : " + Hex.encodeHexString(littleEndianActualX11Bytes));
        boolean isTargetGreaterThan=ByteUtils.fastCompare(expectedTarget, littleEndianActualX11Bytes)==1;
        System.out.println("isTargetGreaterThan : " + isTargetGreaterThan);
        assertTrue(isTargetGreaterThan);
        System.out.println("----------------------------------------------------------------");

    }

    private static byte[] prepareMerkelRoot() throws NoSuchAlgorithmException, IOException {
        byte[] bytes = null;
        try {
            bytes = Hex.decodeHex("da5aa8c3ba44284fee52e1c5c65392af8a483e7427de3b58c3639cdc23265ea0".toCharArray());
        } catch (DecoderException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    private static byte[] get4Bytes(byte[] b) {
        byte[] b1 = new byte[4];
        System.arraycopy(b, 4, b1, 0, 4);
        return b1;
    }

}
