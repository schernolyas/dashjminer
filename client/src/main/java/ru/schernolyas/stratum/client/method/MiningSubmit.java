/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.method;

import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author schernolyas
 */
public class MiningSubmit {

    private String method = "mining.submit";
    private int id = 4;
    private String[] params = new String[5];

    public void setWorkerName(byte[] workerNameBytes) {
        params[0] = Hex.encodeHexString(workerNameBytes);
    }

    public void setJobId(byte[] jobIdBytes) {
        params[1] = Hex.encodeHexString(jobIdBytes);
    }

    public void setExtraNonce2(byte[] extraNonce2Bytes) {
        params[2] = Hex.encodeHexString(extraNonce2Bytes);
    }

    public void setNTime(byte[] nTimeBytes) {
        params[3] = Hex.encodeHexString(nTimeBytes);
    }

    public void setNonce(byte[] nonceBytes) {
        params[4] = Hex.encodeHexString(nonceBytes);
    }

}
