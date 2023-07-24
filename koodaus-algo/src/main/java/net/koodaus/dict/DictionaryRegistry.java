package net.koodaus.dict;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author mzinal
 */
public class DictionaryRegistry implements Serializable {

    private static final long serialVersionUID = 1L;

    private final File baseDir;
    private static final HashMap<String, Dictionary> dictionaries = new HashMap<>();

    public DictionaryRegistry() {
        this.baseDir = null;
    }

    public DictionaryRegistry(File baseDir) {
        this.baseDir = baseDir;
    }

    public DictionaryRegistry(String baseDir) {
        this.baseDir = (baseDir==null || baseDir.length()==0) ? null : new File(baseDir);
    }

    public Dictionary get(String name) {
        Dictionary dict;
        synchronized(dictionaries) {
            dict = dictionaries.get(name);
        }
        if (dict==null) {
            dict = register(MemoryDictionary.load(new File(baseDir, name + ".txt")));
        }
        return dict;
    }

    public Dictionary getOrNull(String name) {
        synchronized(dictionaries) {
            return dictionaries.get(name);
        }
    }

    public Dictionary register(Dictionary dict) {
        synchronized(dictionaries) {
            dictionaries.put(dict.getName(), dict);
        }
        return dict;
    }

    public Dictionary[] resolve(String... names) {
        Dictionary[] localDicts = new Dictionary[names.length];
        for (int pos = 0; pos < names.length; ++pos) {
            localDicts[pos] = get(names[pos]);
        }
        return localDicts;
    }

}
