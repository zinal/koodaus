package net.koodaus.algo;

import java.nio.charset.StandardCharsets;

/**
 * Replacement index generator, CRC32 implementation.
 * 
 * @author zinal
 */
public class FpxCrc32 extends FpxCrcBase {

    private static final long serialVersionUID = 1L;

    public FpxCrc32(byte[] userKey) {
        super(userKey);
    }

    public FpxCrc32(String userKey) {
        super(userKey);
    }

    @Override
    public final long getSeedValue(String value, String iteration) {
        final PureJavaCrc32 crc = new PureJavaCrc32();
        if (userKey != null && userKey.length > 0)
            crc.update(userKey);
        crc.update(value.getBytes(StandardCharsets.UTF_8));
        if (iteration != null)
            crc.update(iteration.getBytes(StandardCharsets.UTF_8));
        return crc.getValue();
    }

}
