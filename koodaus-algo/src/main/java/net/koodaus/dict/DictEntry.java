package net.koodaus.dict;

/**
 * Dictionary element.
 *
 * @author zinal
 */
public class DictEntry {

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

    public String[] getExtras() {
        return extras;
    }

    public void setExtras(String[] extras) {
        this.extras = extras;
    }

}
