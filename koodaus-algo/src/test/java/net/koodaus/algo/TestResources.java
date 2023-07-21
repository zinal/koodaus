package net.koodaus.algo;

import java.io.File;

/**
 *
 * @author zinal
 */
public abstract class TestResources {

    protected File getSamplesDir() {
        return new File(".", "samples").getAbsoluteFile();
    }

}
