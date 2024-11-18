package net.koodaus.udf;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.SplittableRandom;
import net.koodaus.algo.CharClassSet;
import net.koodaus.algo.PureJavaCrc64;

/**
 *
 * @author mzinal
 */
public class TextUdf implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final long keyState;
    private final String ccs;
    private final int minLen;
    private final int maxLen;

    public TextUdf(byte[] userKey, long subkey, CharClassSet ccs, 
            int minLen, int maxLen) {
        if (userKey==null || userKey.length==0) {
            keyState = subkey;
        } else {
            keyState = PureJavaCrc64.update(subkey, userKey, 0, userKey.length);
        }
        this.ccs = ccs.getAsText();
        this.minLen = minLen;
        this.maxLen = maxLen;
    }

    public TextUdf(String userKey, long subkey, CharClassSet ccs, int minLen, int maxLen) {
        this((userKey==null || userKey.length()==0) ?
                (byte[])null : userKey.getBytes(StandardCharsets.UTF_8),
                subkey, ccs, minLen, maxLen);
    }

    public TextUdf(byte[] userKey, CharClassSet ccs, int minLen, int maxLen) {
        this(userKey, 0L, ccs, minLen, maxLen);
    }

    public TextUdf(String userKey, CharClassSet ccs, int minLen, int maxLen) {
        this((userKey==null || userKey.length()==0) ?
                (byte[])null : userKey.getBytes(StandardCharsets.UTF_8),
                0L, ccs, minLen, maxLen);
    }

    public String word(long position) {
        final SplittableRandom random = new SplittableRandom(
                PureJavaCrc64.update(keyState, position));
        return word(random, minLen, maxLen);
    }

    public String phrase(long position) {
        final SplittableRandom random = new SplittableRandom(
                PureJavaCrc64.update(keyState, position));
        final int wordMax = maxLen / 5;
        if (wordMax < 4) {
            return word(random, minLen, maxLen);
        }
        final int wordMin = 3;
        final int totalLen = random.nextInt(minLen, maxLen+1);
        final StringBuilder sb = new StringBuilder();
        while (sb.length() < totalLen) {
            String word = word(random, wordMin, wordMax);
            if (sb.length()>0) {
                sb.append(" ");
            }
            sb.append(word);
        }
        if (sb.length() <= maxLen) {
            return sb.toString();
        }
        return sb.toString().substring(0, maxLen+1);
    }

    private String word(SplittableRandom random, int minWord, int maxWord) {
        int len = random.nextInt(minWord, maxWord+1);
        final StringBuilder sb = new StringBuilder(len);
        while (len-- > 0) {
            sb.append(ccs.charAt(random.nextInt(ccs.length())));
        }
        return sb.toString();
    }

}
