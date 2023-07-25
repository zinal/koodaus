package net.koodaus.udf;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.SplittableRandom;
import net.koodaus.algo.FpxCrc64;
import net.koodaus.algo.FpxIndexerFactory;
import net.koodaus.algo.PureJavaCrc64;
import net.koodaus.dict.Dictionary;
import net.koodaus.dict.DictionaryHarvester;
import net.koodaus.dict.DictionaryRegistry;

/**
 *
 * @author mzinal
 */
public class EmailUdf implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String DOMAINS = "inet-domains";
    public static final String CHARS = "abcdefghijklmnopqrstuvwxyz0123456789._-";

    private final FpxIndexerFactory indexerFactory;
    private final Dictionary dictDomains;
    private final DictionaryHarvester harvestDomains;
    private final long keyState;

    public EmailUdf(DictionaryRegistry dr, String password) {
        indexerFactory = new FpxCrc64(password);
        dictDomains = dr.get(DOMAINS);
        harvestDomains = new DictionaryHarvester(indexerFactory, dictDomains);
        byte[] userKey = password.getBytes(StandardCharsets.UTF_8);
        keyState = PureJavaCrc64.update(0, userKey, 0, userKey.length);
    }

    public EmailUdf(File baseDir, String password) {
        this(new DictionaryRegistry(baseDir), password);
    }

    public EmailUdf(String baseDir, String password) {
        this(new DictionaryRegistry(new File(baseDir)), password);
    }

    public String getEmail(long position) {
        String domain = harvestDomains.harvest(position);
        long newState = PureJavaCrc64.update(keyState, position);
        SplittableRandom sr = new SplittableRandom(newState);
        int nchars = sr.nextInt(3,20);
        final StringBuilder sb = new StringBuilder();
        for (int x=0; x<nchars; x++) {
            sb.append(CHARS.charAt(sr.nextInt(CHARS.length())));
        }
        sb.append("@").append(domain);
        return sb.toString();
    }

}
