/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.blockheader;

import ru.schernolyas.stratum.client.minimg.GlobalObjects;
import ru.schernolyas.stratum.client.utils.ByteUtils;
import ru.schernolyas.stratum.client.utils.NonceTimeUtil;

/**
 * Class for produce Block Header candidate
 * @author schernolyas
 */
public class BlockHeaderCandidateProducer {

    private static final int NONCE_POSITION = 4 + 32 + 32 + 4 + 4;
    private static final int TIME_POSITION = 4 + 32 + 32;
    private final byte[] blockHeaderTemplateCopy = new byte[80];
    private final NonceTimeUtil nonceTimeUtil;

    /**
     * Constructor
     *
     * @param blockHeaderTemplateBytes - bytes of block header template (block
     * header with time and nonce is 0)
     * @param nonceTimeUtil initialized util
     */
    public BlockHeaderCandidateProducer(byte[] blockHeaderTemplateBytes, NonceTimeUtil nonceTimeUtil) {
        System.arraycopy(blockHeaderTemplateBytes, 0, blockHeaderTemplateCopy, 0, 80);
        this.nonceTimeUtil = nonceTimeUtil;
    }

    /**
     * prepare block header
     *
     * @return bytes of block header
     */
    public byte[] produceBlockHeaderCandidate() {
        byte[] littleEndianTime = ByteUtils.littleEndian(nonceTimeUtil.getNTime(GlobalObjects.isTestMode()));
        byte[] littleEndianNonce = ByteUtils.littleEndian(nonceTimeUtil.getNonce(GlobalObjects.isTestMode()));
        System.arraycopy(littleEndianTime, 0, blockHeaderTemplateCopy, TIME_POSITION, 4);
        System.arraycopy(littleEndianNonce, 0, blockHeaderTemplateCopy, NONCE_POSITION, 4);
        return blockHeaderTemplateCopy;

    }

}
