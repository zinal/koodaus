package net.koodaus.dict;

import java.io.Serializable;

/**
 * Dictionary element.
 *
 * @author zinal
 */
public class DictEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    private String value;
    private String[] extras;

    public DictEntry(String value) {
        this.value = value;
    }

    public DictEntry(String value, String[] extras) {
        this.value = value;
        this.extras = extras;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getExtra(int pos) {
        if (extras==null || pos<0 || pos>=extras.length)
            return "";
        return extras[pos];
    }

    public String[] getExtras() {
        return extras;
    }

    public void setExtras(String[] extras) {
        this.extras = extras;
    }

}
