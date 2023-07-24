package net.koodaus.dict;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

/**
 * Dictionary - a collection of elements.
 *
 * @author zinal
 */
public interface Dictionary {

    String getName();

    int size();

    DictEntry get(int pos);

    DictEntry get(String value);

    Stream<DictEntry> toStream();

    static void writeValue(PrintWriter pw, String value) {
        if (value.contains(" ") || value.contains("\t")) {
            pw.append('"');
            pw.append(value);
            pw.append('"');
        } else {
            pw.append(value);
        }
    }

    default void save(String fname) {
        try (OutputStreamWriter fw =
                new OutputStreamWriter(new FileOutputStream(fname), StandardCharsets.UTF_8);
             PrintWriter pw = new PrintWriter(fw)) {
            toStream().forEach(de -> {
                writeValue(pw, de.getValue());
                if (de.getExtras() != null) {
                    for (String extraValue : de.getExtras()) {
                        pw.append("\t");
                        writeValue(pw, extraValue);
                    }
                }
                pw.append("\n");
            });
            pw.close();
        } catch(IOException ix) {
            throw new RuntimeException("Cannot write file " + fname, ix);
        }
    }

    /**
     * Build the new in-memory dictionary by extracting the specified element
     * from the current dictionary's extra data.
     * @param dictName Dictionary name for the new in-memory dictionary
     * @param extraPos Position in the extras data
     * @return New in-memory dictionary
     */
    default MemoryDictionary extractExtra(String dictName, int extraPos) {
        return MemoryDictionary.loadEntries(dictName,
                toStream().map( de -> new DictEntry(de.getExtra(extraPos)) ));
    }

}
