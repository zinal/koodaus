package net.koodaus.algo;

/**
 * Index generator interface
 * @author zinal
 */
public interface FpxIndexGen {

    /**
     * Retrieve the next index in an array of specified size
     * @param charCount Size of an array
     * @return Index value from 0 to size-1
     */
    int nextIndex(int charCount);

}
