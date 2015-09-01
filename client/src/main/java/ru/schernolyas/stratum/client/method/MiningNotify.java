/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.method;

import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author Sergey Chernolyas
 */
public class MiningNotify {

    private static final Logger LOG = Logger.getLogger(MiningNotify.class.getName());

    public static String METHOD_NAME = "mining.notify";
    
    private byte[] jobId;
    private byte[] previousBlockHash;
    private byte[] coinBase1;
    private byte[] coinBase2;
    private byte[][] merkleBranches;
    private byte[] blockVersion;
    private byte[] encodedNetworkDifficulty;
    private byte[] currentTime;
    private boolean cleanJobs;

    

    private static MiningNotify processJsonObject(JsonObject jsonObject) throws DecoderException {
        MiningNotify miningNotify = new MiningNotify();
        JsonArray paramsArray = jsonObject.getJsonArray("params");
        miningNotify.setJobId(Hex.decodeHex(paramsArray.getJsonString(0).getString().toCharArray()));
        LOG.log(Level.INFO, "jobId : {0}", new Object[]{Hex.encodeHexString(miningNotify.getJobId())});
        miningNotify.setPreviousBlockHash(Hex.decodeHex(paramsArray.getJsonString(1).getString().toCharArray()));
        LOG.log(Level.INFO, "previousBlockHash : {0}", new Object[]{Hex.encodeHexString(miningNotify.getPreviousBlockHash())});
        miningNotify.setCoinBase1(Hex.decodeHex(paramsArray.getJsonString(2).getString().toCharArray()));
        LOG.log(Level.INFO, "coinBase1 : {0}", new Object[]{Hex.encodeHexString(miningNotify.getCoinBase1())});
        miningNotify.setCoinBase2(Hex.decodeHex(paramsArray.getJsonString(3).getString().toCharArray()));
        LOG.log(Level.INFO, "coinBase2 : {0}", new Object[]{Hex.encodeHexString(miningNotify.getCoinBase2())});
        JsonArray merkleBranchesArray = paramsArray.getJsonArray(4);
        miningNotify.setMerkleBranches( new byte[merkleBranchesArray.size()][]);
        for (int i = 0; i < merkleBranchesArray.size(); i++) {
            miningNotify.getMerkleBranches()[i] = Hex.decodeHex(merkleBranchesArray.getJsonString(i).getString().toCharArray());
        }
        for (int i = 0; i < miningNotify.getMerkleBranches().length; i++) {
            byte[] merkleBranche = miningNotify.getMerkleBranches()[i];
            LOG.log(Level.INFO, "merkleBranche N {0} : {1}", new Object[]{i, Hex.encodeHexString(merkleBranche)});
        }
        miningNotify.setBlockVersion(Hex.decodeHex(paramsArray.getJsonString(5).getString().toCharArray()));
        LOG.log(Level.INFO, "blockVersion : {0}", new Object[]{Hex.encodeHexString(miningNotify.getBlockVersion())});
        miningNotify.setEncodedNetworkDifficulty(Hex.decodeHex(paramsArray.getJsonString(6).getString().toCharArray()));
        LOG.log(Level.INFO, "encodedNetworkDifficulty : {0}", new Object[]{Hex.encodeHexString(miningNotify.getEncodedNetworkDifficulty())});
        System.out.println("!"+paramsArray.getJsonString(7).getString().length());
        String str = paramsArray.getJsonString(7).getString();
        if ((str.length() & 0x01) != 0) {
            str = "0"+str;
        }
        miningNotify.setCurrentTime(Hex.decodeHex(str.toCharArray()));
        LOG.log(Level.INFO, "currentTime : {0}", new Object[]{Hex.encodeHexString(miningNotify.getCurrentTime())});
        
        miningNotify.setCleanJobs(paramsArray.getBoolean(8));
        LOG.log(Level.INFO, "cleanJobs : {0}", new Object[]{miningNotify.isCleanJobs()});
        return miningNotify;
    }

    public byte[] getJobId() {
        return jobId;
    }

    public void setJobId(byte[] jobId) {
        this.jobId = jobId;
    }

    public byte[] getPreviousBlockHash() {
        return previousBlockHash;
    }

    public void setPreviousBlockHash(byte[] previousBlockHash) {
        this.previousBlockHash = previousBlockHash;
    }

    public byte[] getCoinBase1() {
        return coinBase1;
    }

    public void setCoinBase1(byte[] coinBase1) {
        this.coinBase1 = coinBase1;
    }

    public byte[] getCoinBase2() {
        return coinBase2;
    }

    public void setCoinBase2(byte[] coinBase2) {
        this.coinBase2 = coinBase2;
    }

    public byte[][] getMerkleBranches() {
        return merkleBranches;
    }

    public void setMerkleBranches(byte[][] merkleBranches) {
        this.merkleBranches = merkleBranches;
    }

    public byte[] getBlockVersion() {
        return blockVersion;
    }

    public void setBlockVersion(byte[] blockVersion) {
        this.blockVersion = blockVersion;
    }

    public byte[] getEncodedNetworkDifficulty() {
        return encodedNetworkDifficulty;
    }

    public void setEncodedNetworkDifficulty(byte[] encodedNetworkDifficulty) {
        this.encodedNetworkDifficulty = encodedNetworkDifficulty;
    }

    public byte[] getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(byte[] currentTime) {
        this.currentTime = currentTime;
    }

    public boolean isCleanJobs() {
        return cleanJobs;
    }

    public void setCleanJobs(boolean cleanJobs) {
        this.cleanJobs = cleanJobs;
    }

    public static MiningNotify build(String jsonString) throws DecoderException {
        JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
        JsonObject obj = jsonReader.readObject();
        return processJsonObject(obj);
    }
}
