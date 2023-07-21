package net.koodaus.dict;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;
import net.koodaus.util.KoodausUtil;
import org.apache.commons.text.StringTokenizer;

/**
 * Dictionary - a collection of elements.
 *
 * @author zinal
 */
public class Dictionary {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(Dictionary.class);

    private final String name;
    private final ArrayList<DictEntry> elements = new ArrayList<>();
    private final HashMap<String, DictEntry> search = new HashMap<>();

    public Dictionary(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int size() {
        return elements.size();
    }

    public DictEntry get(int pos) {
        return elements.get(pos);
    }

    public DictEntry get(String value) {
        return search.get(value);
    }

    public Stream<DictEntry> toStream() {
        return elements.stream();
    }

    public static Dictionary load(String fname) {
        String name = new File(fname).getName();
        int ixdot = name.lastIndexOf(".");
        if (ixdot > 0) {
            name = name.substring(0, ixdot);
        }
        return load(name, fname);
    }

    public static Dictionary load(String name, String fname) {
        try {
            return load(name, Files.lines(Paths.get(fname)));
        } catch(IOException ix) {
            throw new RuntimeException("Cannot read file " + fname, ix);
        }
    }

    public static Dictionary load(String name, Stream<String> data) {
        final StringTokenizer st = new StringTokenizer();
        return loadEntries(name, data.map(line -> {
            st.reset(line);
            String[] items = st.getTokenArray();
            if (items.length > 0) {
                final String value = KoodausUtil.unquote(items[0]);
                final String[] extras = new String[items.length - 1];
                for (int i=1; i<items.length; i++) {
                    extras[i-1] = KoodausUtil.unquote(items[i]);
                }
                return new DictEntry(value, extras);
            } else {
                return (DictEntry) null;
            }
        }).filter(value -> (value!=null)));
    }

    public static Dictionary loadEntries(String name, Stream<DictEntry> data) {
        LOG.info("Building dictionary {} ...", name);
        final Dictionary dict = new Dictionary(name);
        data.forEach(de -> {
            if (dict.search.containsKey(de.getValue())) {
                LOG.warn("Duplicate entry {} in dictionary {} - SKIPPED", de.getValue(), name);
            } else {
                dict.elements.add(de);
                dict.search.put(de.getValue(), de);
            }
        });
        LOG.info("Dictionary {} complete, total {} entries", name, dict.size());
        return dict;
    }

}
