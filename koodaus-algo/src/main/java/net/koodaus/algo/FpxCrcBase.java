package net.koodaus.algo;

import java.nio.charset.StandardCharsets;
import java.util.SplittableRandom;

/**
 * Replacement index generator, CRC based implementation.
 *
 * @author mzinal
 */
public abstract class FpxCrcBase implements FpxIndexerFactory {

    private static final long serialVersionUID = 1L;

    protected final byte[] userKey;

    public FpxCrcBase(byte[] userKey) {
        this.userKey = userKey;
    }

    public FpxCrcBase(String userKey) {
        this(userKey.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public FpxIndexer make(String value, String iteration) {
        return new Indexer(getSeedValue(value, iteration));
    }

    abstract long getSeedValue(String value, String iteration);

    public static class Indexer implements FpxIndexer {

        private final SplittableRandom random;

        public Indexer(long seed) {
            this.random = new SplittableRandom(seed);
        }

        /**
         * Retrieve next bit from a MAC value, recycling the MAC as necessary
         * @return true, if bit is 1, false otherwise
         */
        public final boolean getNextBit() {
            return random.nextBoolean();
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
