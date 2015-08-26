/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.utils;

import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author Сергей
 */
public class MerkleTreeUtil {
    private static final Logger LOG = Logger.getLogger(MerkleTreeUtil.class.getName());
    /**
     * 
     * @param md
     * @param doubleHashCoinBase sha256(sha256(CoinBase))
     * @param merkleBranches
     * @return 
     */
    public static byte[] calculate(MessageDigest md,byte[] doubleHashCoinBase,byte[][] merkleBranches) {
        LOG.log(Level.INFO, "------------------------------------------------------");
        byte[] merkleRoot = new byte[32];
        System.arraycopy(doubleHashCoinBase, 0, merkleRoot, 0, 32);
        LOG.log(Level.INFO, "digest : {0}", new Object[]{Hex.encodeHexString(merkleRoot)});
        for (int i = 0; i < merkleBranches.length; i++) {
            byte[] currentHash = new byte[64];
            System.arraycopy(merkleRoot, 0, currentHash, 0, 32);
            LOG.log(Level.INFO, "current merkleRoot : {0}; i: {1}", new Object[]{Hex.encodeHexString(merkleRoot),i});
            byte[] currentMerkleBranche = merkleBranches[i];
            System.arraycopy(currentMerkleBranche, 0, currentHash, 31, 32);
            merkleRoot = md.digest(md.digest(currentHash));
            LOG.log(Level.INFO, "merkleRoot : {0}; i: {1}", new Object[]{Hex.encodeHexString(merkleRoot),i});
        }
        LOG.log(Level.INFO, "finalMerkleRoot : {0}", new Object[]{Hex.encodeHexString(merkleRoot)});
        LOG.log(Level.INFO, "------------------------------------------------------");
        return merkleRoot;
    }
    
}
