package net.koodaus.dict;

import java.util.HashMap;

/**
 *
 * @author mzinal
 */
public class DictionaryRegistry {

    private static final HashMap<String, Dictionary> dictionaries = new HashMap<>();

    public Dictionary get(String name) {
        return dictionaries.get(name);
    }

    public void register(Dictionary dict) {
        dictionaries.put(dict.getName(), dict);
    }

}
