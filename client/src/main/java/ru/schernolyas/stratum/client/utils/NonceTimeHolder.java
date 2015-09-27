/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.utils;

/**
 *
 * @author schernolyas
 */
public interface NonceTimeHolder {
    @Deprecated
    public byte[] getNTime();
    public byte[] getNonce();
    public byte[] getNonce(long offset);
    
}
