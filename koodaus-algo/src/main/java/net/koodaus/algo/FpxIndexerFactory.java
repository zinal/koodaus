package net.koodaus.algo;

/**
 * Index generator factory interface.
 * @author zinal
 */
public interface FpxIndexerFactory {

    /**
     * Create the new index generator based on the input value and iteration value.
     * @param value Input value, must not be null
     * @param iteration Iteration value, must be null on initial iteration
     * @return New index generator for the current input and iteration values
     */
    FpxIndexer make(String value, String iteration);

    default FpxIndexer make(String value, long iteration) {
        if (iteration==0) {
            return make(value, null);
        }
        return make(value, Long.toHexString(iteration));
    }

    default FpxIndexer make(String value, int iteration) {
        if (iteration==0) {
            return make(value, null);
        }
        return make(value, Integer.toHexString(iteration));
    }

}
