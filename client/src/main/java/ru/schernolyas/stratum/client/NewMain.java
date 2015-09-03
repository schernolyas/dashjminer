/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import ru.schernolyas.stratum.client.dto.BlockHeader;
import ru.schernolyas.stratum.client.blockheader.BlockHeaderCandidateProducer;
import ru.schernolyas.stratum.client.blockheader.BlockHeaderTemplateProducer;
import ru.schernolyas.stratum.client.method.Initial;
import ru.schernolyas.stratum.client.method.MiningNotify;
import ru.schernolyas.stratum.client.method.SetDifficulty;
import ru.schernolyas.stratum.client.minimg.MiningManager;
import ru.schernolyas.stratum.client.utils.ByteUtils;
import ru.schernolyas.stratum.client.utils.CoinBaseUtil;
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
            String responseStr3 = "{\"id\":null,\"method\":\"mining.notify\",\"params\":[\"3ae3\",\"" + Hex.encodeHexString(ByteUtils.swapOrder(Hex.decodeHex("0000000000108d4b9231f4ec99ab5dc970b6ec740745f44eee0754f67d598ac3".toCharArray()))) + "\","
                    + "\"01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff200316f904043900de5508\","
                    + "\"0d2f6e6f64655374726174756d2f0000000002cbd50411000000001976a914edc3ed0229526ea4c728d714b5289f97e7f63a2188acc5d50411000000001976a9141e3ad74cc9b5c1281464892199f2170bedb9c40788ac00000000\","
                    + "[\"9c864cae10cb55a97dd9a04cd1d22144b688377f505f9bc701925f65baedb430\",\"836f0f697b6d3a6283c7c264a5a144c893e7b077ef4aea87dbb4f32d81ac86c5\"],\"00000002\",\"1b10cf42\","
                    + "\"" + requiredTime + "\",true]}";
            System.out.println(responseStr3);

            Initial initial = Initial.build(responseStr1);
            MiningNotify miningNotify = MiningNotify.build(responseStr3);
            SetDifficulty setDifficulty = SetDifficulty.build(responseStr2);
            LOG.log(Level.INFO, "current difficulty : 1) byte: {0}; 2) decimal: {1}",
                    new Object[]{Hex.encodeHexString(setDifficulty.getByteDifficulty()), setDifficulty.getDecimalDifficulty()});

            byte[] currentTarget = DifficultyUtil.calculateTarget(setDifficulty);
            LOG.log(Level.INFO, "currentTarget : {0}", new Object[]{Hex.encodeHexString(currentTarget)});
            long testNonceValue = 5628506L;
            //NonceTimeUtil nonceUtil = new NonceTimeUtil(miningNotify.getCurrentTime(), testNonceValue);
            NonceTimeUtil nonceUtil = new NonceTimeUtil(Hex.decodeHex(requiredTime.toCharArray()), testNonceValue);

            BlockHeaderTemplateProducer blockHeaderTemplateProducer = new BlockHeaderTemplateProducer(miningNotify, initial);
            byte[] blockHeaderTemplate = blockHeaderTemplateProducer.produceBlockHeaderTemplate();
            LOG.log(Level.INFO, "--------------------------------------------------------");
            LOG.log(Level.INFO, "block header template : {0}", new Object[]{Hex.encodeHexString(blockHeaderTemplate)});
            MiningManager miningManager = new MiningManager();
            miningManager.startMining(blockHeaderTemplate, currentTarget,nonceUtil);
            

            BlockHeaderCandidateProducer blockHeaderCandidateProducer = new BlockHeaderCandidateProducer(blockHeaderTemplate, nonceUtil);
            byte[] blockHeaderCandidate = blockHeaderCandidateProducer.produceBlockHeaderCandidate();

            LOG.log(Level.INFO, "--------------------------------------------------------");

            LOG.log(Level.INFO, "block header  : {0}", new Object[]{Hex.encodeHexString(blockHeaderCandidate)});

            byte[] x11Hash = X11Util.calculate(blockHeaderCandidate);

            LOG.log(Level.INFO, "currentTarget: {0}; littleEndian x11 value : {1}; ",
                    new Object[]{Hex.encodeHexString(currentTarget),
                        Hex.encodeHexString(ByteUtils.littleEndian(x11Hash))});

        } catch (DecoderException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

}
