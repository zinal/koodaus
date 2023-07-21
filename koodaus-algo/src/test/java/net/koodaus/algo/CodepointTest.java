package net.koodaus.algo;

import org.junit.Assert;
import org.junit.Test;
import net.koodaus.util.DsMaskUtil;

/**
 *
 * @author zinal
 */
public class CodepointTest {

    @Test
    public void checkCodepoint02() {
        String v = DsMaskUtil.fromCodepoint(2);
        Assert.assertNotEquals("?", v);
        Assert.assertNotEquals(" ", v);
        Assert.assertEquals(1, v.length());
        Assert.assertEquals(2, (int) v.charAt(0));
    }

}
