package net.koodaus.util;

import java.util.Objects;

/**
 * Miscellaneous utility methods which are hard to put elsewhere.
 * @author zinal
 */
public class KoodausUtil {

    /**
     * Empty string - treated as a missing value.
     */
    public static final String NONE = "";

    public static final String EOL = System.getProperty("line.separator");

    public static String safe(String value) {
        return (value==null) ? NONE : value.trim();
    }

    public static String lower(String value) {
        return safe(value).toLowerCase();
    }

    public static String unquote(String s) {
        if (s.startsWith("\"") && s.endsWith("\"") && s.length()>1) {
            s = s.substring(1, s.length()-1);
        }
        return s.trim();
    }

    public static boolean equalsCI(String a, String b) {
        return (a == b) || safe(a).equalsIgnoreCase(safe(b));
    }

    public static boolean equalsNL(String a, String b) {
        return Objects.equals(a, b)
                || (a==null && (b==null || b.length()==0))
                || (b==null && (a==null || a.length()==0));
    }

    public static boolean safeEquals(String a, String b) {
        return (a==b) || safe(a).equals(safe(b));
    }

    public static int hashCode(String o) {
        return o != null ? lower(o).hashCode() : 0;
    }
    
    /**
     * Convert the code point to a string representation
     * @param sb String builder to append the code point to, or null to allocate a new one
     * @param cp Code point to be converted
     * @return Same string builder object as passed on input, or a new one if null was passed
     */
    public static StringBuilder appendCodepoint(StringBuilder sb, int cp) {
        if (sb==null)
            sb = new StringBuilder();
        if (Character.isBmpCodePoint(cp)) {
            sb.append((char) cp);
        } else if (Character.isValidCodePoint(cp)) {
            sb.append(Character.highSurrogate(cp));
            sb.append(Character.lowSurrogate(cp));
        } else {
            sb.append('?');
        }
        return sb;
    }

    /**
     * Convert the code point to a string representation
     * @param cp Code point to be converted
     * @return String representation of the code point on input
     */
    public static String fromCodepoint(int cp) {
        return appendCodepoint(null, cp).toString();
    }

    /**
     * Convert the input value to boolean
     * @param val input value
     * @return true or false (default false)
     */
    public static boolean toBoolean(String val) {
        if (val==null || val.length()==0)
            return false;
        char c = val.charAt(0);
        return ('1'==c)
                || ('Y'==c) || ('y'==c)
                || ('T'==c) || ('t'==c);
    }

    /**
     * Resolve pathname potentially starting with "~/".
     * @param path Input path
     * @return Resolved path
     */
    public static String resolvePath(String path) {
        if (path.startsWith("~/"))
            return System.getProperty("user.home") + path.substring(1);
        return path;
    }

    public static RuntimeException toRE(String message, Throwable ex) {
        String msg = message + ": "
                + ( (ex.getMessage() == null) ?
                    ex.getClass().getSimpleName() :
                    ex.getMessage() );
        return new RuntimeException(msg, ex);
    }

}
