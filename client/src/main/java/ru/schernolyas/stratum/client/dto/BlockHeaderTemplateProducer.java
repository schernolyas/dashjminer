/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.dto;

import ru.schernolyas.stratum.client.utils.ByteUtils;
import ru.schernolyas.stratum.client.utils.NonceTimeUtil;

/**
 *
 * @author schernolyas
 */
public class BlockHeaderTemplateProducer {

    private static final int NONCE_POSITION = 4 + 32 + 32 + 4 + 4;
    private static final int TIME_POSITION = 4 + 32 + 32;
    private byte[] blockHeaderTemplateCopy = new byte[80];
    private NonceTimeUtil nonceTimeUtil;
    ByteUtils byteUtils = new ByteUtils();

    public BlockHeaderTemplateProducer(byte[] blockHeaderTemplate, NonceTimeUtil nonceTimeUtil) {
        System.arraycopy(blockHeaderTemplate, 0, blockHeaderTemplateCopy, 0, 80);
        this.nonceTimeUtil = nonceTimeUtil;
    }

    public byte[] produceBlockHeader() {
        byte[] littleEndianTime = ByteUtils.factory().littleEndian(nonceTimeUtil.getNTime());
        byte[] littleEndianNonce = ByteUtils.factory().littleEndian(nonceTimeUtil.getNonce());
        System.arraycopy(littleEndianTime, 0, blockHeaderTemplateCopy, TIME_POSITION, 4);
        System.arraycopy(littleEndianNonce, 0, blockHeaderTemplateCopy, NONCE_POSITION, 4);
        return blockHeaderTemplateCopy;

    }

}
