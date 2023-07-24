package net.koodaus.algo;

import java.nio.charset.StandardCharsets;

/**
 * Naive Java-only FPE algorithm implementation.
 * @author mzinal
 */
public class MaskFPE extends FpxAlgo {

    public static final String DEFAULT_KEY = "ваттерпежек0змА";
    public static final String HMAC_NAME = FpxFPE.HMAC_NAME;

    /**
     * Minimal constructor.
     */
    public MaskFPE() {
        this(CharClassSet.DEFAULT_RUSSIAN);
    }

    /**
     * Helper constructor.
     * @param cset Character set class definition
     */
    public MaskFPE(CharClassSet cset) {
        this(cset, "");
    }

    /**
     * Helper constructor.
     * @param cset Character set class definition
     * @param userKey User key string - if null or empty, default key will be used instead
     */
    public MaskFPE(CharClassSet cset, String userKey) {
        this(cset, userKey, 0, 0, false);
    }

    /**
     * Helper constructor.
     * @param cset Character set class definition
     * @param userKey User key string - if null or empty, default key will be used instead
     * @param skipBefore Number of characters to be skipped unmasked at the beginning
     * @param skipAfter Number of characters to be skipped unmasked at the end
     */
    public MaskFPE(CharClassSet cset, String userKey,
            int skipBefore, int skipAfter) {
        this(cset, userKey, skipBefore, skipAfter, false);
    }

    /**
     * Helper constructor.
     * @param cset Character set class definition
     * @param userKey User key string - if null or empty, default key will be used instead
     * @param skipBefore Number of characters to be skipped unmasked at the beginning
     * @param skipAfter Number of characters to be skipped unmasked at the end
     * @param allowSameVal Allow unmodified input
     */
    public MaskFPE(CharClassSet cset, String userKey,
            int skipBefore, int skipAfter, boolean allowSameVal) {
        this(cset,
                (userKey==null) ? (byte[])null : userKey.getBytes(StandardCharsets.UTF_8),
                skipBefore, skipAfter, allowSameVal);
    }

    /**
     * Real constructor implementation.
     * @param cset Character set class definition
     * @param userKey User key bytes - if null or empty, default key will be used instead
     * @param skipBefore Number of characters to be skipped unmasked at the beginning
     * @param skipAfter Number of characters to be skipped unmasked at the end
     * @param allowSameVal Allow unmodified input
     */
    public MaskFPE(CharClassSet cset, byte[] userKey,
            int skipBefore, int skipAfter, boolean allowSameVal) {
        super(cset, userKey, skipBefore, skipAfter, allowSameVal, 
                new FpxFPE(fixKey(userKey), HMAC_NAME));
    }

    public static byte[] fixKey(byte[] userKey) {
        if (userKey==null || userKey.length==0)
            userKey = DEFAULT_KEY.getBytes(StandardCharsets.UTF_8);
        return userKey;
    }

}
