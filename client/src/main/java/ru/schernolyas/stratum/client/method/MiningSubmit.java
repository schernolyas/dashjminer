/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.method;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.Arrays;
import org.apache.commons.codec.binary.Hex;
import ru.schernolyas.stratum.client.blockheader.BlockHeaderCandidateProducer;
import ru.schernolyas.stratum.client.net.NetHandler;
import ru.schernolyas.stratum.client.utils.ByteUtils;
import ru.schernolyas.stratum.client.utils.ExtraNonce2Util;

/**
 *
 * @author schernolyas
 */
public class MiningSubmit {

    private String method = "mining.submit";
    private int id = 4;
    private String workerName;
    private String extraNonce2;
    private String nTime;
    private String nonce;
    private String jobId;

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public void setJobId(byte[] jobIdBytes) {
        this.jobId = Hex.encodeHexString(jobIdBytes);
    }

    public void setExtraNonce2(byte[] extraNonce2Bytes) {
        this.extraNonce2 = Hex.encodeHexString(extraNonce2Bytes);
    }

    public void setNTime(byte[] nTimeBytes) {
        this.nTime = Hex.encodeHexString(nTimeBytes);
    }

    public void setNonce(byte[] nonceBytes) {
        this.nonce = Hex.encodeHexString(nonceBytes);
    }

    public String getMethod() {
        return method;
    }

    public int getId() {
        return id;
    }

    public String getExtraNonce2() {
        return extraNonce2;
    }

    public String getnTime() {
        return nTime;
    }

    public String getNonce() {
        return nonce;
    }

    public String getJobId() {
        return jobId;
    }

    public String toJsonString() {
        JsonObject obj = new JsonObject();
        obj.put("id", id);
        obj.put("method", method);
        
        obj.put("params",  new JsonArray(Arrays.asList(getWorkerName(),getJobId(),getExtraNonce2(),getnTime(),getNonce())));        
        return obj.encode().concat("\n");
    }

    public static MiningSubmit build(byte[] blockHeader, Initial initial, MiningNotify miningNotify) {
        MiningSubmit submit = new MiningSubmit();
        submit.setJobId(miningNotify.getJobId());
        submit.setExtraNonce2(ExtraNonce2Util.createExtraNonce2(initial.getExtraNonce2Size()));
        byte[] littleEndianResultNonce = new byte[4];
        System.arraycopy(blockHeader, BlockHeaderCandidateProducer.NONCE_POSITION, littleEndianResultNonce, 0, 4);
        submit.setNonce(ByteUtils.bigEndian(littleEndianResultNonce));
        byte[] littleEndianResultTime = new byte[4];
        System.arraycopy(blockHeader, BlockHeaderCandidateProducer.TIME_POSITION, littleEndianResultTime, 0, 4);
        submit.setNTime(ByteUtils.bigEndian(littleEndianResultTime));
        submit.setWorkerName(NetHandler.WORKER_NAME);
        return submit;
    }

}
