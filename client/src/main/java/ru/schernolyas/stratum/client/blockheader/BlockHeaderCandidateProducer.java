/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.blockheader;

import ru.schernolyas.stratum.client.utils.ByteUtils;
import ru.schernolyas.stratum.client.utils.NonceTimeHolder;

/**
 * Class for produce Block Header candidate
 *
 * @author schernolyas
 */
public class BlockHeaderCandidateProducer {

    public static final int NONCE_POSITION = 4 + 32 + 32 + 4 + 4;
    public static final int TIME_POSITION = 4 + 32 + 32;
    private final byte[] blockHeaderTemplateCopy = new byte[80];
    private final NonceTimeHolder nonceTimeHolder;

    /**
     * Constructor
     *
     * @param blockHeaderTemplateBytes - bytes of block header template (block
     * header with time and nonce is 0)
     * @param nonceTimeHolder initialized util
     */
    public BlockHeaderCandidateProducer(byte[] blockHeaderTemplateBytes, NonceTimeHolder nonceTimeHolder) {
        System.arraycopy(blockHeaderTemplateBytes, 0, this.blockHeaderTemplateCopy, 0, 80);
        this.nonceTimeHolder = nonceTimeHolder;
    }

    public void updateBlockTemplate(byte[] blockHeaderTemplateBytes) {
        synchronized (this.blockHeaderTemplateCopy) {
            System.arraycopy(blockHeaderTemplateBytes, 0, this.blockHeaderTemplateCopy, 0, 80);
        }
    }

    /**
     * prepare block header
     *
     * @return bytes of block header
     */
    public byte[] produceBlockHeaderCandidate() {
        byte[] littleEndianTime = ByteUtils.littleEndian(nonceTimeHolder.getNTime());
        byte[] littleEndianNonce = ByteUtils.littleEndian(nonceTimeHolder.getNonce());
        System.arraycopy(littleEndianTime, 0, blockHeaderTemplateCopy, TIME_POSITION, 4);
        System.arraycopy(littleEndianNonce, 0, blockHeaderTemplateCopy, NONCE_POSITION, 4);
        return blockHeaderTemplateCopy;
    }

}
