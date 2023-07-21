package net.koodaus.algo;

/**
 * Index generator factory interface.
 * @author zinal
 */
public interface FpxIndexFactory {

    /**
     * Create the new index generator based on the input value and iteration value.
     * @param value Input value, must not be null
     * @param iteration Iteration value, must be null on initial iteration
     * @return New index generator for the current input and iteration values
     */
    FpxIndexGen make(String value, String iteration);

}
