package net.koodaus.udf;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author mzinal
 */
public class UuidUdfTest {

    private static final String[] VALUES = {
        "baf239dee6140d7e0000000000000001",
        "b1b239dee6140d7e000000000000000d",
        "597239dee6140d7e00000000000000a9",
        "7f3fb9dee6140d7e0000000000000895",
        "79a1a9dee6140d7e0000000000006f91",
        "c652deaee6140d7e000000000005aa5d",
        "4299f9eee6140d7e000000000049a6b9",
        "e677d62c36140d7e0000000003bd7765",
        "8ce9f363e6140d7e00000000309f1021",
        "5f1c904265740d7e000000027813d1ad",
        "039b4879e0140d7e000000201901a5c9",
        "916775c579a5bd7e000001a145156b35",
        "4fb297a5ab087d7e00001530821671b1",
        "28017dc2f2addcce000113769b23c5fd"
    };

    @Test
    public void test1() {
        final UuidUdf uu = new UuidUdf("служил гаврило колумнистом");
        int pos = 0;
        for (long v = 1; v < 999999999999999L; v *= 13L) {
            String xv = uu.long2str(v);
            Assert.assertEquals(VALUES[pos], xv);
            pos += 1;
        }
    }

}
