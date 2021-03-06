/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.blockheader;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import ru.schernolyas.stratum.client.dto.BlockHeaderImpl;
import ru.schernolyas.stratum.client.method.Initial;
import ru.schernolyas.stratum.client.method.MiningNotify;
import ru.schernolyas.stratum.client.utils.ByteUtils;
import ru.schernolyas.stratum.client.utils.CoinBaseUtil;
import ru.schernolyas.stratum.client.utils.MerkleTreeUtil;

/**
 *
 * @author schernolyas
 */
public class BlockHeaderTemplateProducer {

    private static final Logger LOG = Logger.getLogger(BlockHeaderTemplateProducer.class.getName());

    private final MiningNotify miningNotify;
    private final Initial initial;

    public BlockHeaderTemplateProducer(MiningNotify miningNotify, Initial initial) {
        this.miningNotify = miningNotify;
        this.initial = initial;
    }

    public byte[] produceBlockHeaderTemplate() throws IOException, NoSuchAlgorithmException, DecoderException {

        byte[] finalMerkleRoot = calculateMerkelRoot();
        LOG.log(Level.INFO, "finalMerkleRoot : {0}", new Object[]{Hex.encodeHexString(finalMerkleRoot)});

        BlockHeaderImpl blockHeader = new BlockHeaderImpl();
        blockHeader.setVersion(miningNotify.getBlockVersion());
        blockHeader.setMerkleRoot(ByteUtils.preparePrevHash(finalMerkleRoot));
        blockHeader.setPrevHash(miningNotify.getPreviousBlockHash());
        blockHeader.setnBit(miningNotify.getEncodedNetworkDifficulty());
        blockHeader.setnTime(miningNotify.getCurrentTime());
        return blockHeader.toBlockHeader();
    }

    protected byte[] calculateMerkelRoot() throws NoSuchAlgorithmException, IOException {
        MessageDigest sha256md = MessageDigest.getInstance("SHA-256");
        //http://thedestitutedeveloper.blogspot.ru/2014/03/stratum-mining-block-headers-worked.html
        byte[] coinBase = CoinBaseUtil.produceCoinBase(miningNotify, initial);
        byte[] doubleHashCoinBase = sha256md.digest(sha256md.digest(coinBase));
        byte[] finalMerkleRoot = MerkleTreeUtil.calculate(sha256md, doubleHashCoinBase, miningNotify.getMerkleBranches());
        return finalMerkleRoot;

    }

}
