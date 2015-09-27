/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.utils;

import fr.cryptohash.BLAKE512;
import fr.cryptohash.BMW512;
import fr.cryptohash.CubeHash512;
import fr.cryptohash.ECHO512;
import fr.cryptohash.Groestl512;
import fr.cryptohash.JH512;
import fr.cryptohash.Keccak512;
import fr.cryptohash.Luffa512;
import fr.cryptohash.SHAvite512;
import fr.cryptohash.SIMD512;
import fr.cryptohash.Skein512;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.pool2.ObjectPool;
import ru.schernolyas.stratum.client.cryptopool.CryptoHolder;
import ru.schernolyas.stratum.client.cryptopool.CryptoPool;

/**
 *
 * @author Сергей
 */
public class X11Util {

    private static final Logger LOG = Logger.getLogger(X11Util.class.getName());

    /**
     * <code>
     *
     *   pdata - currect block header
     *   <code>
     *   int kk=0;
     *	for (; kk < 32; kk++)
     *	{
     *		be32enc(&endiandata[kk], ((uint32_t*)pdata)[kk]);
     *	};
     *
     *       </code>
     *
     * </code>
     *
     * @param preparedX11BlockHeaderCandidate
     * @return
     * @throws Exception
     */
    public static byte[] calculate(byte[] preparedX11BlockHeaderCandidate) {

        ObjectPool<CryptoHolder> cryptoHolderPool = CryptoPool.getCryptoHolderPool();
        CryptoHolder holder = null;
        byte[] hash = null;
        try {
            holder = cryptoHolderPool.borrowObject();
            hash = holder.getBlake512().digest(preparedX11BlockHeaderCandidate);
            hash = holder.getBmw512().digest(hash);
            hash = holder.getGroestl512().digest(hash);
            hash = holder.getSkein512().digest(hash);
            hash = holder.getJh512().digest(hash);
            hash = holder.getKeccak512().digest(hash);
            hash = holder.getLuffa512().digest(hash);
            hash = holder.getCubeHash512().digest(hash);
            hash = holder.getShaVite512().digest(hash);
            hash = holder.getSimd512().digest(hash);
            hash = holder.getEcho512().digest(hash);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error", e);
        } finally {
            if (holder != null) {
                try {
                    cryptoHolderPool.returnObject(holder);
                } catch (Exception e) {
                    LOG.log(Level.SEVERE, "Error", e);
                }
            }
        }

        return hash!=null ? Arrays.copyOfRange(hash, 31, 63) : null;
    }
}
