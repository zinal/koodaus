package net.koodaus.algo;

import java.nio.charset.StandardCharsets;
import java.util.SplittableRandom;

/**
 * Replacement index generator, CRC32 implementation.
 * @author zinal
 */
public class FpxIndexHash implements FpxIndexFactory {

    private final byte[] userKey;

    public FpxIndexHash(byte[] userKey) {
        this.userKey = userKey;
    }

    @Override
    public FpxIndexGen make(String value, String iteration) {
        return new IndexGen(getSeedValue(value, iteration));
    }

    public final long getSeedValue(String value, String iteration) {
        final PureJavaCrc32 crc = new PureJavaCrc32();
        if (userKey != null && userKey.length > 0)
            crc.update(userKey);
        crc.update(value.getBytes(StandardCharsets.UTF_8));
        if (iteration != null)
            crc.update(iteration.getBytes(StandardCharsets.UTF_8));
        return crc.getValue();
    }

    public static class IndexGen implements FpxIndexGen {

        private final SplittableRandom random;

        public IndexGen(long seed) {
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
        public int nextIndex(int charCount) {
            if (charCount < 2) {
                return 0;
            }
            // Compute the number of bits required
            int maxVal = 1;
            int bitCount = 0;
            while (maxVal <= charCount) {
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
            return index % charCount;
        }

    }

}
