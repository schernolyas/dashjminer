/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.utils;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.logging.Logger;

/**
 *
 * @author Сергей
 */
public class MerkleTreeUtil {
    private static final Logger LOG = Logger.getLogger(MerkleTreeUtil.class.getName());
    /**
     * <pre>
     *{@code
     * // Generate merkle root 
     *	sha256d(merkle_root, sctx->job.coinbase, sctx->job.coinbase_size);
     *	for (i = 0; i < sctx->job.merkle_count; i++) {
     * 		memcpy(merkle_root + 32, sctx->job.merkle[i], 32);
     *  		sha256d(merkle_root, merkle_root, 64);
     * 	}
     *}
     *</pre>
     * @param md
     * @param doubleHashCoinBase sha256(sha256(CoinBase))
     * @param merkleBranches
     * @return 
     * @throws java.io.IOException 
     */
    public static byte[] calculate(MessageDigest md,byte[] doubleHashCoinBase,byte[][] merkleBranches) throws IOException {
        
        byte[] merkleRoot = doubleHashCoinBase;
        for (byte[] merkleBranche : merkleBranches) {
            merkleRoot = md.digest(md.digest(ByteUtils.concat(merkleRoot, merkleBranche)));
        }
        return merkleRoot;
    }
    
}
