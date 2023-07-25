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
public class UtilUdf implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private final long keyState;

    public UtilUdf(byte[] userKey) {
        if (userKey==null || userKey.length==0) {
            keyState = 0L;
        } else {
            keyState = PureJavaCrc64.update(0, userKey, 0, userKey.length);
        }
    }

    public UtilUdf(String userKey) {
        this((userKey==null || userKey.length()==0) ?
                null : userKey.getBytes(StandardCharsets.UTF_8));
    }

    public UUID long2uid(long v) {
        return new UUID(PureJavaCrc64.update(keyState, v), v);
    }

    public String long2str(long v) {
        final ByteBuffer out = ByteBuffer.allocate(2 * Long.BYTES);
        out.putLong(PureJavaCrc64.update(keyState, v));
        out.putLong(v);
        return Base64.getUrlEncoder().encodeToString(out.array()).substring(0, 22);
    }

    public String firstname(String fullname) {
        String[] parts = fullname.split(" ");
        if (parts.length > 1) {
            final StringBuilder sb = new StringBuilder();
            for (int pos=0; pos<parts.length; pos++) {
                if (sb.length() > 0)
                    sb.append(" ");
                sb.append(parts[pos]);
            }
            return sb.toString();
        }
        return fullname;
    }

    public String lastname(String fullname) {
        String[] parts = fullname.split(" ");
        if (parts!=null && parts.length > 0)
            return parts[0];
        return fullname;
    }

}
