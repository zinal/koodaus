package net.koodaus.app;

import net.koodaus.dict.DictEntry;
import net.koodaus.dict.Dictionary;

/**
 *
 * @author zinal
 */
public class DictConv {
    
    private static String[] toLower(String[] input) {
        if (input==null || input.length==0) {
            return new String[0];
        }
        String[] output = new String[input.length];
        for (int i=0; i<input.length; ++i) {
            if (input[i]==null) {
                output[i] = null;
            } else {
                output[i] = input[i].toLowerCase();
            }
        }
        return output;
    }

    private static String[] toUpper(String[] input) {
        if (input==null || input.length==0) {
            return new String[0];
        }
        String[] output = new String[input.length];
        for (int i=0; i<input.length; ++i) {
            if (input[i]==null) {
                output[i] = null;
            } else {
                output[i] = input[i].toUpperCase();
            }
        }
        return output;
    }

    private static String[] parent1(String name) {
        if (name.endsWith("ий")) {
            String base = name.substring(0, name.length()-2);
            return new String[]{name + "иевич", name + "иевна"};
        }
        return new String[]{};
    }

    public static void main(String[] args) {
        if (args.length!=3) {
            System.err.println("USAGE: DictConv.class sourcedict.txt targetdict.txt ACTION");
            System.err.println("\tACTION: LOWER UPPER VALUE PARENT1");
            System.exit(2);
        }
        try {
            Dictionary input = Dictionary.load(args[0]);
            Dictionary output;
            String actionCode = args[2];
            if ("LOWER".equalsIgnoreCase(actionCode)) {
                output = Dictionary.loadEntries(input.getName(),
                    input.toStream().map(de -> new DictEntry(de.getValue().toLowerCase(), toLower(de.getExtras()))));
            } else if ("UPPER".equalsIgnoreCase(actionCode)) {
                output = Dictionary.loadEntries(input.getName(),
                    input.toStream().map(de -> new DictEntry(de.getValue().toUpperCase(), toUpper(de.getExtras()))));
            } else if ("VALUE".equalsIgnoreCase(actionCode)) {
                output = Dictionary.loadEntries(input.getName(),
                    input.toStream().map(de -> new DictEntry(de.getValue())));
            } else if ("PARENT1".equalsIgnoreCase(actionCode)) {
                output = Dictionary.loadEntries(input.getName(),
                    input.toStream().map(de -> new DictEntry(de.getValue(), parent1(de.getValue()))));
            } else {
                throw new IllegalArgumentException("Unsupported action: " + actionCode);
            }
            output.save(args[1]);
        } catch(Exception ex) {
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }

}
