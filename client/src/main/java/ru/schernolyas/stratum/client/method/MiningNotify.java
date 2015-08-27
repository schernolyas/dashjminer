/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.method;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonArray;
import javax.json.JsonObject;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author Sergey Chernolyas
 */
public class MiningNotify {

    private static final Logger LOG = Logger.getLogger(MiningNotify.class.getName());

    public static String METHOD_NAME = "mining.notify";
    private String inJson;
    private JsonObject jsonObject;
    private byte[] jobId;
    private byte[] previousBlockHash;
    private byte[] coinBase1;
    private byte[] coinBase2;
    private byte[][] merkleBranches;
    private byte[] blockVersion;
    private byte[] encodedNetworkDifficulty;
    private byte[] currentTime;
    private boolean cleanJobs;

    public MiningNotify(String inJson, JsonObject jsonObject) throws DecoderException {
        this.inJson = inJson;
        this.jsonObject = jsonObject;
        processJsonObject();

    }

    private void processJsonObject() throws DecoderException {
        JsonArray paramsArray = this.jsonObject.getJsonArray("params");
        this.jobId = Hex.decodeHex(paramsArray.getJsonString(0).getString().toCharArray());
        LOG.log(Level.INFO, "jobId : {0}", new Object[]{Hex.encodeHexString(jobId)});
        this.previousBlockHash = Hex.decodeHex(paramsArray.getJsonString(1).getString().toCharArray());
        LOG.log(Level.INFO, "previousBlockHash : {0}", new Object[]{Hex.encodeHexString(previousBlockHash)});
        this.coinBase1 = Hex.decodeHex(paramsArray.getJsonString(2).getString().toCharArray());
        LOG.log(Level.INFO, "coinBase1 : {0}", new Object[]{Hex.encodeHexString(coinBase1)});
        this.coinBase2 = Hex.decodeHex(paramsArray.getJsonString(3).getString().toCharArray());
        LOG.log(Level.INFO, "coinBase2 : {0}", new Object[]{Hex.encodeHexString(coinBase2)});
        JsonArray merkleBranchesArray = paramsArray.getJsonArray(4);
        this.merkleBranches = new byte[merkleBranchesArray.size()][];
        for (int i = 0; i < merkleBranchesArray.size(); i++) {
            merkleBranches[i] = Hex.decodeHex(merkleBranchesArray.getJsonString(i).getString().toCharArray());
        }
        for (int i = 0; i < merkleBranches.length; i++) {
            byte[] merkleBranche = merkleBranches[i];
            LOG.log(Level.INFO, "merkleBranche N {0} : {1}", new Object[]{i, Hex.encodeHexString(merkleBranche)});
        }
        this.blockVersion = Hex.decodeHex(paramsArray.getJsonString(5).getString().toCharArray());
        LOG.log(Level.INFO, "blockVersion : {0}", new Object[]{Hex.encodeHexString(blockVersion)});
        this.encodedNetworkDifficulty = Hex.decodeHex(paramsArray.getJsonString(6).getString().toCharArray());
        LOG.log(Level.INFO, "encodedNetworkDifficulty : {0}", new Object[]{Hex.encodeHexString(encodedNetworkDifficulty)});
        System.out.println("!"+paramsArray.getJsonString(7).getString().length());
        String str = paramsArray.getJsonString(7).getString();
        if ((str.length() & 0x01) != 0) {
            str = "0"+str;
        }
        this.currentTime = Hex.decodeHex(str.toCharArray());
        LOG.log(Level.INFO, "currentTime : {0}", new Object[]{Hex.encodeHexString(currentTime)});
        
        this.cleanJobs = paramsArray.getBoolean(8);
        LOG.log(Level.INFO, "cleanJobs : {0}", new Object[]{cleanJobs});
    }

    public byte[] getJobId() {
        return jobId;
    }

    public byte[] getPreviousBlockHash() {
        return previousBlockHash;
    }

    public byte[] getCoinBase1() {
        return coinBase1;
    }

    public byte[] getCoinBase2() {
        return coinBase2;
    }

    public byte[][] getMerkleBranches() {
        return merkleBranches;
    }

    public byte[] getBlockVersion() {
        return blockVersion;
    }

    public byte[] getEncodedNetworkDifficulty() {
        return encodedNetworkDifficulty;
    }

    public byte[] getCurrentTime() {
        return currentTime;
    }

    public boolean isCleanJobs() {
        return cleanJobs;
    }

}
