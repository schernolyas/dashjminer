/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.dto;

/**
 *
 * @author Сергей
 */
public class BlockHeader {
    private static final byte[] DEFAULT_4_BYTES = new byte[]{0,0,0,0};
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
    
    public byte[] toBlockHeader() {
        byte[] block = new byte[80];
        System.arraycopy(version, 0, block, 0, 4);
        System.arraycopy(prevHash, 0, block, 4, 32);
        System.arraycopy(merkleRoot, 0, block, (4+32), 32);
        System.arraycopy(nTime, 0, block, (4+32+32), 4);
        System.arraycopy(nBit, 0, block, (4+32+32+4), 4);
        System.arraycopy(nonce, 0, block, (4+32+32+4+4), 4);
        return block;
    }
    
    
    
}
