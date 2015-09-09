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
import ru.schernolyas.stratum.client.minimg.GlobalObjects;
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

        MessageDigest sha256md = MessageDigest.getInstance("SHA-256");
        //http://thedestitutedeveloper.blogspot.ru/2014/03/stratum-mining-block-headers-worked.html
        byte[] coinBase = CoinBaseUtil.produceCoinBase(miningNotify, initial);
        byte[] doubleHashCoinBase = sha256md.digest(sha256md.digest(coinBase));
        byte[] finalMerkleRoot = MerkleTreeUtil.calculate(sha256md, doubleHashCoinBase, miningNotify.getMerkleBranches());

        LOG.log(Level.INFO, "finalMerkleRoot : {0}", new Object[]{Hex.encodeHexString(finalMerkleRoot)});

        BlockHeaderImpl blockHeader = new BlockHeaderImpl();
        blockHeader.setVersion(ByteUtils.littleEndian(miningNotify.getBlockVersion()));
        //LOG.log(Level.INFO, "blockHeader.getVersion() : {0}", new Object[]{Hex.encodeHexString(blockHeader.getVersion())});
        blockHeader.setMerkleRoot(finalMerkleRoot);
       // LOG.log(Level.INFO, "blockHeader.getMerkleRoot() : {0}", new Object[]{Hex.encodeHexString(blockHeader.getMerkleRoot())});
        blockHeader.setPrevHash(miningNotify.getPreviousBlockHash());
        //LOG.log(Level.INFO, "blockHeader.getPrevHash() : {0}", new Object[]{Hex.encodeHexString(blockHeader.getPrevHash())});
        blockHeader.setnBit(ByteUtils.littleEndian(miningNotify.getEncodedNetworkDifficulty()));
        //LOG.log(Level.INFO, "blockHeader.getnBit() : {0}", new Object[]{Hex.encodeHexString(blockHeader.getnBit())});

        return blockHeader.toBlockHeader();
    }

}
