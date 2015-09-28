/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import ru.schernolyas.stratum.client.method.MiningNotify;
import ru.schernolyas.stratum.client.method.SetDifficulty;

/**
 * @see
 * http://www.javaworld.com/article/2077257/learn-java/floating-point-arithmetic.html
 * @author Sergey Chernolyas
 */
public class DifficultyUtil {

    private static final Logger LOG = Logger.getLogger(DifficultyUtil.class.getName());
    private static final BigDecimal CONSTANT_DEVISOR = new BigDecimal("4294967296.0");
    private static final BigDecimal CONSTANT_DEVIDEND = new BigDecimal("4294901760.0");

    
    /**
     * <code>
     * void diff_to_target(uint32_t *target, double diff) {
     *uint64_t m;
     *int k;
     *
     *for (k = 6; k > 0 && diff > 1.0; k--)
     *   diff = diff / 4294967296.0;
     *
     *m = 4294901760.0 / diff;
     *if (m == 0 && k == 6)
     *    memset(target, 0xff, 32);
     *else {
     *    memset(target, 0, 32);
     *    uint32_t m1, m2;
     *    m1 = (uint32_t) m;
     *    m2 = (uint32_t) (m >> 32);
     *    target[k] = m1;
     *    target[k + 1] = m2;
     *}
     *}
     * </code>
     *
     *
     * reimplement 'diff_to_target' function
     *
     * @param decimalDifficulty value of difficulty from command 'set_difficulty' 
     * @return
     */

    public static byte[] calculateTargetByDifficulty(BigDecimal decimalDifficulty) {
        byte[] target = null;
        BigDecimal diff = new BigDecimal(decimalDifficulty.doubleValue());
        int k = 6;
        for (; k > 0 && diff.compareTo(BigDecimal.ONE) == 1; k--) {
            diff = diff.divide(CONSTANT_DEVISOR, 10, RoundingMode.HALF_UP);
        }
        BigDecimal m = CONSTANT_DEVIDEND.divide(diff, 10, RoundingMode.HALF_UP);
        if (k == 0 && m.compareTo(BigDecimal.ZERO) == 0) {
            target = new byte[32];
            Arrays.fill(target, (byte) 0xff);
        } else {
            BigInteger m1 = m.toBigInteger();
            BigInteger m2 = m1.shiftRight(32);
            ByteBuffer buffer = ByteBuffer.allocate(32);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            for (int i = 0; i < 8; i++) {
                if (!(i == k || i == (k + 1))) {
                    buffer.putInt(0);
                } else if (i == k) {
                    buffer.putInt(m1.intValue());
                } else if (i == (k + 1)) {
                    buffer.putInt(m2.intValue());
                }
            }
            //System.err.println("target:" + Hex.encodeHexString(buffer.array()));
            target = buffer.array();
        }
        return target;

    }

}
