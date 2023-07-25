package net.koodaus.udf;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import net.koodaus.algo.PureJavaCrc64;

/**
 *
 * @author mzinal
 */
public class UuidUdf implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private final long keyState;

    public UuidUdf(byte[] userKey) {
        if (userKey==null || userKey.length==0) {
            keyState = 0L;
        } else {
            keyState = PureJavaCrc64.update(0, userKey, 0, userKey.length);
        }
    }

    public UuidUdf(String userKey) {
        this((userKey==null || userKey.length()==0) ?
                null : userKey.getBytes(StandardCharsets.UTF_8));
    }

    public UUID long2uid(long v) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(v);
        byte[] xv = buffer.array();
        long crc = PureJavaCrc64.update(keyState, xv, 0, xv.length);
        return new UUID(crc, v);
    }

    public String long2str(long v) {
        ByteBuffer temp = ByteBuffer.allocate(Long.BYTES);
        temp.putLong(v);
        byte[] xv = temp.array();
        long crc = PureJavaCrc64.update(keyState, xv, 0, xv.length);
        ByteBuffer out = ByteBuffer.allocate(2 * Long.BYTES);
        out.putLong(crc);
        out.putLong(v);
        return Base64.getUrlEncoder().encodeToString(out.array()).substring(0, 22);
    }

}
