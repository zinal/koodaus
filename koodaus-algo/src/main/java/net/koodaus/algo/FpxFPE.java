package net.koodaus.algo;

import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Replacement index generator, FPE implementation.
 * @author zinal
 */
public class FpxFPE implements FpxIndexerFactory {

    public static final String HMAC_NAME = "HmacSHA512";

    private final String hmacName;
    private final SecretKeySpec keySpec;
    private transient Mac mac;

    public FpxFPE(byte[] userKey, String hmacName) {
        try {
            this.hmacName = hmacName;
            this.keySpec = new SecretKeySpec(userKey, hmacName);
            this.mac = Mac.getInstance(hmacName);
            this.mac.init(this.keySpec);
        } catch(Exception ex) {
            this.mac = null;
            throw new RuntimeException("Cannot initialize MAC " + hmacName, ex);
        }
    }

    public FpxFPE(String userKey, String hmacName) {
        this(userKey.getBytes(StandardCharsets.UTF_8), hmacName);
    }

    public FpxFPE(String userKey) {
        this(userKey.getBytes(StandardCharsets.UTF_8), HMAC_NAME);
    }

    public FpxFPE(byte[] userKey) {
        this(userKey, HMAC_NAME);
    }

    @Override
    public FpxIndexer make(String value, String iteration) {
        return new Indexer(getMacValue(value, iteration));
    }

    private Mac getMac() {
        if (mac==null) {
            try {
                mac = Mac.getInstance(hmacName);
                mac.init(keySpec);
            } catch(Exception ex) {
                mac = null;
                throw new RuntimeException("Cannot initialize MAC " + hmacName, ex);
            }
        }
        return mac;
    }

    public final byte[] getMacValue(String value, String iteration) {
        Mac xmac = null;
        try {
            xmac = getMac();
            if (iteration != null && iteration.length() > 0) {
                xmac.update(iteration.getBytes(StandardCharsets.UTF_8));
                xmac.update((byte)1);
            }
            return xmac.doFinal(value.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            // MAC object cleanup for the worst case
            try {
                if (xmac!=null)
                    xmac.doFinal();
            } catch (Exception tmp) {}
            throw new RuntimeException("Failed to compute MAC", ex);
        }
    }

    public static class Indexer implements FpxIndexer {

        private final byte[] macValue;
        private BitSet macBits = null;
        private int bitsPosition = 0;

        public Indexer(byte[] macValue) {
            this.macValue = macValue;
        }

        /**
         * Retrieve next bit from a MAC value, recycling the MAC as necessary
         * @return true, if bit is 1, false otherwise
         */
        public boolean getNextBit() {
            if (macBits == null || bitsPosition >= macBits.length()) {
                macBits = BitSet.valueOf(macValue);
                bitsPosition = 0;
            }
            return macBits.get(bitsPosition++);
        }

        @Override
        public int nextIndex(int sz) {
            if (sz < 1) {
                return -1;
            }
            if (sz < 2) {
                return 0;
            }
            // Compute the number of bits required
            int maxVal = 1;
            int bitCount = 0;
            while (maxVal <= sz) {
                bitCount += 1;
                maxVal *= 2;
            }
            // Generate the index of desired size based on MAC bits.
            int index = 0;
            maxVal = 1;
            for (int pos = 0; pos < bitCount; ++pos) {
                if (getNextBit()) {
                    index += maxVal;
                }
                maxVal *= 2;
            }
            return index % sz;
        }

    }

}
