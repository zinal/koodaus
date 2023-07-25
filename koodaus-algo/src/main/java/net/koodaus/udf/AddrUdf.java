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
public class AddrUdf implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String CITIES = "addr-cities";
    public static final String STREETS = "addr-streets";

    private final FpxIndexerFactory indexerFactory;
    private final Dictionary addrCities;
    private final Dictionary addrStreets;
    private final DictionaryHarvester harvestCities;
    private final DictionaryHarvester harvestStreets;

    public AddrUdf(DictionaryRegistry dr, String password) {
        indexerFactory = new FpxCrc64(password);
        addrCities = dr.get(CITIES);
        harvestCities = new DictionaryHarvester(indexerFactory, addrCities);
        addrStreets = dr.get(STREETS);
        harvestStreets = new DictionaryHarvester(indexerFactory, addrStreets);
    }

    public AddrUdf(File baseDir, String password) {
        this(new DictionaryRegistry(baseDir), password);
    }

    public AddrUdf(String baseDir, String password) {
        this(new DictionaryRegistry(new File(baseDir)), password);
    }

    public String getCity(long position) {
        return harvestCities.harvest(position * 7);
    }

    public String getStreet(long position) {
        return harvestStreets.harvest(position * 3);
    }
}
