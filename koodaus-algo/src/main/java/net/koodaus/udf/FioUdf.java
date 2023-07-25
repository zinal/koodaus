package net.koodaus.udf;

import java.io.File;
import java.io.Serializable;
import net.koodaus.algo.FpxCrc64;
import net.koodaus.algo.FpxIndexerFactory;
import net.koodaus.dict.Dictionary;
import net.koodaus.dict.DictionaryHarvester;
import net.koodaus.dict.DictionaryRegistry;

/**
 *
 * @author mzinal
 */
public class FioUdf implements Serializable {

    public static final String NAMES_FIRST_M = "names-first-male";
    public static final String NAMES_MIDDLE_M = "names-middle-male";
    public static final String NAMES_LAST_M = "names-last-male";
    public static final String NAMES_FIRST_F = "names-first-female";
    public static final String NAMES_MIDDLE_F = "names-middle-female";
    public static final String NAMES_LAST_F = "names-last-female";

    private final FpxIndexerFactory indexerFactory;

    private final Dictionary namesFirstM;
    private final Dictionary namesMiddleM;
    private final Dictionary namesLastM;
    private final DictionaryHarvester harvestM;

    private final Dictionary namesFirstF;
    private final Dictionary namesMiddleF;
    private final Dictionary namesLastF;
    private final DictionaryHarvester harvestF;

    public FioUdf(DictionaryRegistry dr, String password) {
        indexerFactory = new FpxCrc64(password);
        namesFirstM = dr.get(NAMES_FIRST_M);
        namesLastM = dr.get(NAMES_LAST_M);
        namesFirstF = dr.get(NAMES_FIRST_F);
        namesLastF = dr.get(NAMES_LAST_F);
        Dictionary d = dr.getOrNull(NAMES_MIDDLE_M);
        if (d==null) {
            d = namesFirstM.extractExtra(NAMES_MIDDLE_M, 0);
            dr.register(d);
        }
        namesMiddleM = d;
        d = dr.getOrNull(NAMES_MIDDLE_F);
        if (d==null) {
            d = namesFirstM.extractExtra(NAMES_MIDDLE_F, 1);
            dr.register(d);
        }
        namesMiddleF = d;
        harvestM = new DictionaryHarvester(indexerFactory, namesLastM, namesFirstM, namesMiddleM);
        harvestF = new DictionaryHarvester(indexerFactory, namesLastF, namesFirstF, namesMiddleF);
    }

    public FioUdf(File baseDir, String password) {
        this(new DictionaryRegistry(baseDir), password);
    }

    public FioUdf(String baseDir, String password) {
        this(new DictionaryRegistry(new File(baseDir)), password);
    }

    public String call(boolean male, long position) {
        if (male) {
            return harvestM.harvest(position);
        } else {
            return harvestF.harvest(position);
        }
    }

    public String callCode12(int sex, long position) {
        return call((sex==1), position);
    }

}
