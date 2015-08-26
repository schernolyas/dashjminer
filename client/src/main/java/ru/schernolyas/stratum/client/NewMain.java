/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import ru.schernolyas.stratum.client.dto.BlockHeader;
import ru.schernolyas.stratum.client.method.Initial;
import ru.schernolyas.stratum.client.method.MiningNotify;
import ru.schernolyas.stratum.client.method.SetDifficulty;
import ru.schernolyas.stratum.client.utils.ByteUtils;
import ru.schernolyas.stratum.client.utils.DifficultyUtil;
import ru.schernolyas.stratum.client.utils.MerkleTreeUtil;
import ru.schernolyas.stratum.client.utils.NonceTimeUtil;
import ru.schernolyas.stratum.client.utils.X11Util;

;

/**
 * Награда за блок определяется по формуле 2222222/(((Difficulty+2600)/9)^2)
 *
 * @author Сергей
 * @see https://www.btcguild.com/new_protocol.php
 */
public class NewMain {

    private static final Logger LOG = Logger.getLogger(NewMain.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        try {
            MessageDigest sha256md = MessageDigest.getInstance("SHA-256");
            String responseStr1 = "{\"id\":1,\"result\":[[[\"mining.set_difficulty\",\"deadbeefcafebabe76df0c0000000000\"],[\"mining.notify\",\"deadbeefcafebabe76df0c0000000000\"]],\"100578b3\",4],\"error\":null}";
            String responseStr2 = "{\"id\":null,\"method\":\"mining.set_difficulty\",\"params\":[0.03125]}";
            String responseStr3 = "{\"id\":null,\"method\":\"mining.notify\",\"params\":[\"3ae3\",\"482722cd12a737f0e1bc3579227edce63a9cf1d4f39f206f00116e6800000000\",\"01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff200316f904043900de5508\",\"0d2f6e6f64655374726174756d2f0000000002cbd50411000000001976a914edc3ed0229526ea4c728d714b5289f97e7f63a2188acc5d50411000000001976a9141e3ad74cc9b5c1281464892199f2170bedb9c40788ac00000000\",[\"4809f8d8f1f4add7cc0a0aa7297885285d1d3064812afb745b4b69a256e81440\",\"305451c5617d738395b43574347970da9c0d5802b42a1bdb4d9ec2c2111575fd\"],\"00000003\",\"1b18f349\",\"55de0050\",true]}";

            JsonReader jsonReader = Json.createReader(new StringReader(responseStr1));
            JsonObject obj = jsonReader.readObject();
            Initial initial = new Initial(responseStr1, obj);
            jsonReader = Json.createReader(new StringReader(responseStr3));
            obj = jsonReader.readObject();
            MiningNotify miningNotify = new MiningNotify(responseStr3, obj);
            jsonReader = Json.createReader(new StringReader(responseStr2));
            obj = jsonReader.readObject();
            SetDifficulty setDifficulty = new SetDifficulty(responseStr2, obj);
            LOG.log(Level.INFO, "current difficulty : {0}", new Object[]{Hex.encodeHexString(setDifficulty.getDifficulty())});
            BigInteger currentTarget = DifficultyUtil.calculateTarget(setDifficulty.getDifficulty());
            LOG.log(Level.INFO, "currentTarget : {0}", new Object[]{Hex.encodeHexString(currentTarget.toByteArray())});

            ByteArrayOutputStream coinBaseOs = new ByteArrayOutputStream();
            coinBaseOs.write(miningNotify.getCoinBase1());
            coinBaseOs.write(initial.getExtraNonce1());
            coinBaseOs.write(initial.getExtraNonce2());
            coinBaseOs.write(miningNotify.getCoinBase2());
            coinBaseOs.flush();

            byte[] coinBase = coinBaseOs.toByteArray();
            byte[] doubleHashCoinBase = sha256md.digest(sha256md.digest(coinBase));
            byte[] finalMerkleRoot = MerkleTreeUtil.calculate(sha256md, doubleHashCoinBase, miningNotify.getMerkleBranches());

            BlockHeader blockHeader = new BlockHeader();
            NonceTimeUtil nonceUtil = new NonceTimeUtil(miningNotify.getCurrentTime());
            blockHeader.setVersion(ByteUtils.littleEndian(miningNotify.getBlockVersion()));
            LOG.log(Level.INFO, "blockHeader.getVersion() : {0}", new Object[]{Hex.encodeHexString(blockHeader.getVersion())});
            blockHeader.setMerkleRoot(finalMerkleRoot);
            blockHeader.setPrevHash(ByteUtils.preparePrevHash(miningNotify.getPreviousBlockHash()));
            LOG.log(Level.INFO, "blockHeader.getPrevHash() : {0}", new Object[]{Hex.encodeHexString(blockHeader.getPrevHash())});
            blockHeader.setnTime(ByteUtils.littleEndian(nonceUtil.getNTime()));
            LOG.log(Level.INFO, "blockHeader.getnTime() : {0}", new Object[]{Hex.encodeHexString(blockHeader.getnTime())});
            blockHeader.setnBit(ByteUtils.littleEndian(miningNotify.getEncodedNetworkDifficulty()));
            LOG.log(Level.INFO, "blockHeader.getnBit() : {0}", new Object[]{Hex.encodeHexString(blockHeader.getnBit())});
            blockHeader.setNonce(nonceUtil.getNTime());
            LOG.log(Level.INFO, "blockHeader.getNonce() : {0}", new Object[]{Hex.encodeHexString(blockHeader.getNonce())});
            byte[] block = blockHeader.toBlock();
            byte[] x11Hash = ByteUtils.littleEndian(X11Util.calculate(block));
            BigInteger x11Value = new BigInteger(x11Hash);
            LOG.log(Level.INFO, "currentTarget: {0}; x11 hash : {1}; compare result: {2}",
                    new Object[]{Hex.encodeHexString(currentTarget.toByteArray()),
                        Hex.encodeHexString(x11Hash), currentTarget.compareTo(x11Value)});

        } catch (DecoderException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

}
