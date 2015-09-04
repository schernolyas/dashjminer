/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.utils;

import java.io.IOException;
import ru.schernolyas.stratum.client.method.Initial;
import ru.schernolyas.stratum.client.method.MiningNotify;

/**
 *
 * @author Сергей
 */
public class CoinBaseUtil {

    public static byte[] produceCoinBase(MiningNotify miningNotify, Initial initial) throws IOException {
        return ByteUtils.concat(miningNotify.getCoinBase1(),initial.getExtraNonce1(),
                ExtraNonce2Util.createExtraNonce2(initial.getExtraNonce2Size()),
                miningNotify.getCoinBase2());
    }

}
