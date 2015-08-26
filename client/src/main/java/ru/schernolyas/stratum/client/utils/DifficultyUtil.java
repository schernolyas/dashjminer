/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.utils;

import java.math.BigInteger;

/**
 *
 * @author Сергей
 */
public class DifficultyUtil {
    public static final BigInteger MAX_DIFFICULTY=BigInteger.valueOf(0xFFFF).shiftLeft(208);
    public static BigInteger calculateTarget(byte[] currentDifficultyBytes) {
        BigInteger currentDifficulty = new BigInteger(currentDifficultyBytes);
        return MAX_DIFFICULTY.divide(currentDifficulty);
    }
    
}
