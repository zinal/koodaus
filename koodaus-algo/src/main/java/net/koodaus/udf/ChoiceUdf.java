package net.koodaus.udf;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.SplittableRandom;
import net.koodaus.algo.PureJavaCrc64;

/**
 *
 * @author mzinal
 */
public class ChoiceUdf implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private final long keyState;

    public ChoiceUdf(byte[] userKey) {
        if (userKey==null || userKey.length==0) {
            keyState = 0L;
        } else {
            keyState = PureJavaCrc64.update(0, userKey, 0, userKey.length);
        }
    }

    public ChoiceUdf(String userKey) {
        this((userKey==null || userKey.length()==0) ?
                null : userKey.getBytes(StandardCharsets.UTF_8));
    }

    public int chooseInt(long position, int... vs) {
        if (vs==null || vs.length==0) {
            throw new IllegalArgumentException("Empty input set");
        }
        long newState = PureJavaCrc64.update(keyState, position);
        int pos = new SplittableRandom(newState).nextInt(vs.length);
        return vs[pos];
    }

    public int makeInt(long position, int minInclusive, int maxExclusive) {
        long newState = PureJavaCrc64.update(keyState, position);
        return new SplittableRandom(newState).nextInt(minInclusive, maxExclusive);
    }

    public String zip(long position) {
        long newState = PureJavaCrc64.update(keyState, position);
        int value = new SplittableRandom(newState).nextInt(100000, 999999 + 1);
        return Integer.toString(value);
    }

    // 79036305064
    public String phone(long position) {
        long newState = PureJavaCrc64.update(keyState, position);
        long value = new SplittableRandom(newState).nextLong(9000000000L, 9999999999L + 1);
        return "7" + Long.toString(value);
    }

}
