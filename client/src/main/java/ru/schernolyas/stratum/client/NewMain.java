/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
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
import ru.schernolyas.stratum.client.utils.NonceTimeUtil;
import ru.schernolyas.stratum.client.utils.X11Util;

;

/**
 *
 * @author Sergey Chernolyas
 * @see https://www.btcguild.com/new_protocol.php
 */
public class NewMain {

    private static final Logger LOG = Logger.getLogger(NewMain.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ParseException {
        try {
            MessageDigest sha256md = MessageDigest.getInstance("SHA-256");
            /*String responseStr1 = "{\"id\":1,\"result\":[[[\"mining.set_difficulty\",\"deadbeefcafebabe76df0c0000000000\"],[\"mining.notify\",\"deadbeefcafebabe76df0c0000000000\"]],\"100578b3\",4],\"error\":null}";
             String responseStr2 = "{\"id\":null,\"method\":\"mining.set_difficulty\",\"params\":[0.03125]}";
             String responseStr3 = "{\"id\":null,\"method\":\"mining.notify\",\"params\":[\"3ae3\",\"482722cd12a737f0e1bc3579227edce63a9cf1d4f39f206f00116e6800000000\",\"01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff200316f904043900de5508\",\"0d2f6e6f64655374726174756d2f0000000002cbd50411000000001976a914edc3ed0229526ea4c728d714b5289f97e7f63a2188acc5d50411000000001976a9141e3ad74cc9b5c1281464892199f2170bedb9c40788ac00000000\",[\"4809f8d8f1f4add7cc0a0aa7297885285d1d3064812afb745b4b69a256e81440\",\"305451c5617d738395b43574347970da9c0d5802b42a1bdb4d9ec2c2111575fd\"],\"00000003\",\"1b18f349\",\"55de0050\",true]}"; 
             */
            long seconds = 1405041879L;
            String requiredTime = Long.toHexString(seconds);
            System.out.println("calendar: " + requiredTime);
            
            String responseStr1 = "{\"id\":1,\"result\":[[[\"mining.set_difficulty\",\"deadbeefcafebabe76df0c0000000000\"],[\"mining.notify\",\"deadbeefcafebabe76df0c0000000000\"]],\"100578b3\",4],\"error\":null}";
            String responseStr2 = "{\"id\":null,\"method\":\"mining.set_difficulty\",\"params\":[3898.66485602]}";
            String responseStr3 = "{\"id\":null,\"method\":\"mining.notify\",\"params\":[\"3ae3\",\"" + Hex.encodeHexString(new ByteUtils().swapOrder(Hex.decodeHex("0000000000108d4b9231f4ec99ab5dc970b6ec740745f44eee0754f67d598ac3".toCharArray()))) + "\","
                    + "\"01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff200316f904043900de5508\","
                    + "\"0d2f6e6f64655374726174756d2f0000000002cbd50411000000001976a914edc3ed0229526ea4c728d714b5289f97e7f63a2188acc5d50411000000001976a9141e3ad74cc9b5c1281464892199f2170bedb9c40788ac00000000\","
                    + "[\"9c864cae10cb55a97dd9a04cd1d22144b688377f505f9bc701925f65baedb430\",\"836f0f697b6d3a6283c7c264a5a144c893e7b077ef4aea87dbb4f32d81ac86c5\"],\"00000002\",\"1b10cf42\","
                    + "\"" + requiredTime + "\",true]}";
            System.out.println(responseStr3);

            JsonReader jsonReader = Json.createReader(new StringReader(responseStr1));
            JsonObject obj = jsonReader.readObject();
            Initial initial = new Initial(responseStr1, obj);
            jsonReader = Json.createReader(new StringReader(responseStr3));
            obj = jsonReader.readObject();
            MiningNotify miningNotify = new MiningNotify(responseStr3, obj);
            jsonReader = Json.createReader(new StringReader(responseStr2));
            obj = jsonReader.readObject();
            SetDifficulty setDifficulty = new SetDifficulty(responseStr2, obj);
            LOG.log(Level.INFO, "current difficulty : 1) byte: {0}; 2) decimal: {1}",
                    new Object[]{Hex.encodeHexString(setDifficulty.getByteDifficulty()), setDifficulty.getDecimalDifficulty()});

            byte[] currentTarget = DifficultyUtil.calculateTarget(setDifficulty);
            LOG.log(Level.INFO, "currentTarget : {0}", new Object[]{Hex.encodeHexString(currentTarget)});

            ByteArrayOutputStream coinBaseOs = new ByteArrayOutputStream();
            coinBaseOs.write(miningNotify.getCoinBase1());
            coinBaseOs.write(initial.getExtraNonce1());
            coinBaseOs.write(initial.getExtraNonce2());
            coinBaseOs.write(miningNotify.getCoinBase2());
            coinBaseOs.flush();

            // byte[] coinBase = coinBaseOs.toByteArray();
            //byte[] doubleHashCoinBase = sha256md.digest(sha256md.digest(coinBase));
            //byte[] finalMerkleRoot = MerkleTreeUtil.calculate(sha256md, doubleHashCoinBase, miningNotify.getMerkleBranches());
            byte[] finalMerkleRoot = new ByteUtils().swapOrder(Hex.decodeHex("43eb305e7a85ec9d27b3724dab6b2ede5111d54f4568a03d4181231fbd356e81".toCharArray()));
            LOG.log(Level.INFO, "finalMerkleRoot : {0}", new Object[]{Hex.encodeHexString(finalMerkleRoot)});
            BlockHeader blockHeader = new BlockHeader();
            ByteUtils byteUtils = new ByteUtils();
            NonceTimeUtil nonceUtil = new NonceTimeUtil(miningNotify.getCurrentTime());
            LOG.log(Level.INFO, "nonceUtil.getNTime() : {0}", new Object[]{Hex.encodeHexString(nonceUtil.getNTime())});
            blockHeader.setVersion(byteUtils.littleEndian(miningNotify.getBlockVersion()));
            LOG.log(Level.INFO, "blockHeader.getVersion() : {0}", new Object[]{Hex.encodeHexString(blockHeader.getVersion())});
            blockHeader.setMerkleRoot(byteUtils.littleEndian(byteUtils.swapOrder(finalMerkleRoot)));
            LOG.log(Level.INFO, "blockHeader.getMerkleRoot() : {0}", new Object[]{Hex.encodeHexString(blockHeader.getMerkleRoot())});
            blockHeader.setPrevHash(byteUtils.littleEndian(byteUtils.swapOrder(miningNotify.getPreviousBlockHash())));
            LOG.log(Level.INFO, "blockHeader.getPrevHash() : {0}", new Object[]{Hex.encodeHexString(blockHeader.getPrevHash())});
            blockHeader.setnTime(byteUtils.littleEndian(nonceUtil.getNTime()));
            LOG.log(Level.INFO, "blockHeader.getnTime() : {0}", new Object[]{Hex.encodeHexString(blockHeader.getnTime())});
            blockHeader.setnBit(byteUtils.littleEndian(miningNotify.getEncodedNetworkDifficulty()));
            LOG.log(Level.INFO, "blockHeader.getnBit() : {0}", new Object[]{Hex.encodeHexString(blockHeader.getnBit())});
            blockHeader.setNonce(byteUtils.littleEndian(nonceUtil.getNonce()));
            LOG.log(Level.INFO, "blockHeader.getNonce() : {0}", new Object[]{Hex.encodeHexString(blockHeader.getNonce())});
            byte[] blockHeaderBytes = blockHeader.toBlockHeader();
            LOG.log(Level.INFO, "--------------------------------------------------------");
            LOG.log(Level.INFO, "block header : {0}", new Object[]{Hex.encodeHexString(blockHeaderBytes)});
            byte[] x11Hash = X11Util.calculate(blockHeaderBytes);
            
            LOG.log(Level.INFO, "currentTarget: {0}; littleEndian x11 value : {1}; ",
                    new Object[]{Hex.encodeHexString(currentTarget),
                        Hex.encodeHexString(new ByteUtils().littleEndian(x11Hash))});

        } catch (DecoderException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

}
