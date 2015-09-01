/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import ru.schernolyas.stratum.client.method.Initial;
import ru.schernolyas.stratum.client.method.MiningNotify;

/**
 *
 * @author Сергей
 */
public class CoinBaseUtil {

    public static byte[] produceCoinBase(MiningNotify miningNotify, Initial initial) throws IOException {
        ByteArrayOutputStream coinBaseOs = new ByteArrayOutputStream();
        coinBaseOs.write(miningNotify.getCoinBase1());
        coinBaseOs.write(initial.getExtraNonce1());
        coinBaseOs.write(initial.getExtraNonce2());
        coinBaseOs.write(miningNotify.getCoinBase2());
        coinBaseOs.flush();
        return coinBaseOs.toByteArray();
    }

}
