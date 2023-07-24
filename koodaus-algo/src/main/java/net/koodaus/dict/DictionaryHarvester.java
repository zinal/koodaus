package net.koodaus.dict;

import net.koodaus.algo.FpxIndexer;
import net.koodaus.algo.FpxIndexerFactory;

/**
 * Комбайнёр для справочников.
 * 
 * @author mzinal
 */
public class DictionaryHarvester {

    public static final String DEFAULT_SEPARATOR = " ";

    private final String separator;
    private final FpxIndexerFactory indexerFactory;
    private final Dictionary[] dicts;

    public DictionaryHarvester(String separator, FpxIndexerFactory indexerFactory,
            Dictionary... dicts) {
        this.separator = separator;
        this.indexerFactory = indexerFactory;
        this.dicts = dicts;
    }

    public DictionaryHarvester(String separator, FpxIndexerFactory indexerFactory,
            DictionaryRegistry reg, String... names) {
        this(separator, indexerFactory, reg.resolve(names));
    }

    public DictionaryHarvester(FpxIndexerFactory indexerFactory, Dictionary... dicts) {
        this(DEFAULT_SEPARATOR, indexerFactory, dicts);
    }

    public DictionaryHarvester(FpxIndexerFactory indexerFactory,
            DictionaryRegistry reg, String... names) {
        this(DEFAULT_SEPARATOR, indexerFactory, reg.resolve(names));
    }

    public String harvest(long position) {
        final FpxIndexer indexer = indexerFactory.make("", position);
        final StringBuilder sb = new StringBuilder();
        for (Dictionary dict : dicts) {
            if (sb.length() > 0)
                sb.append(separator);
            DictEntry de = dict.get(indexer.nextIndex(dict.size()));
            sb.append(de.getValue());
        }
        return sb.toString();
    }

}
