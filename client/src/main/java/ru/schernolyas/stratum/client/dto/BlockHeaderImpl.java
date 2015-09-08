/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.dto;

import java.io.IOException;
import ru.schernolyas.stratum.client.utils.ByteUtils;

/**
 *
 * @author Сергей
 */
public class BlockHeaderImpl implements BlockHeader {

    private static final byte[] DEFAULT_4_BYTES = new byte[]{0, 0, 0, 0};
    private byte[] version;
    private byte[] prevHash;
    private byte[] merkleRoot;
    private byte[] nTime = DEFAULT_4_BYTES;
    private byte[] nBit;
    private byte[] nonce = DEFAULT_4_BYTES;

    public byte[] getVersion() {
        return version;
    }

    public void setVersion(byte[] version) {
        this.version = version;
    }

    public byte[] getPrevHash() {
        return prevHash;
    }

    public void setPrevHash(byte[] prevHash) {
        this.prevHash = prevHash;
    }

    public byte[] getMerkleRoot() {
        return merkleRoot;
    }

    public void setMerkleRoot(byte[] merkleRoot) {
        this.merkleRoot = merkleRoot;
    }

    public byte[] getnTime() {
        return nTime;
    }

    public void setnTime(byte[] nTime) {
        this.nTime = nTime;
    }

    public byte[] getnBit() {
        return nBit;
    }

    public void setnBit(byte[] nBit) {
        this.nBit = nBit;
    }

    public byte[] getNonce() {
        return nonce;
    }

    public void setNonce(byte[] nonce) {
        this.nonce = nonce;
    }

    public byte[] toBlockHeader() throws IOException {
        return ByteUtils.concat(version, prevHash, merkleRoot, nTime, nBit, nonce);
    }

    public static BlockHeaderImpl build() {
        BlockHeaderImpl header = new BlockHeaderImpl();
        return header;

    }

}
