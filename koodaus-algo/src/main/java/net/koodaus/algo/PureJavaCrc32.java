package net.koodaus.algo;

/**
 *
 * @author zinal
 */
public class PureJavaCrc32 extends org.apache.commons.codec.digest.PureJavaCrc32 {
    
    public final void update(byte[] b) {
        update(b, 0, b.length);
    }

}
