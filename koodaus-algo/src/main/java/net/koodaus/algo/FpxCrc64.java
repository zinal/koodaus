package net.koodaus.algo;

import java.nio.charset.StandardCharsets;

/**
 * Replacement index generator, CRC64 implementation.
 * 
 * @author zinal
 */
public class FpxCrc64 extends FpxCrcBase {

    private static final long serialVersionUID = 1L;

    public FpxCrc64(byte[] userKey) {
        super(userKey);
    }

    public FpxCrc64(String userKey) {
        super(userKey);
    }

    @Override
    public final long getSeedValue(String value, String iteration) {
        final PureJavaCrc64 crc = new PureJavaCrc64();
        if (userKey != null && userKey.length > 0)
            crc.update(userKey);
        crc.update(value.getBytes(StandardCharsets.UTF_8));
        if (iteration != null)
            crc.update(iteration.getBytes(StandardCharsets.UTF_8));
        return crc.getValue();
    }

}
