package net.koodaus.udf;

import org.junit.Assert;
import org.junit.Test;

import net.koodaus.dict.DictionaryRegistry;

/**
 *
 * @author mzinal
 */
public class FioUdfTest {

    @Test
    public void test1() {
        final DictionaryRegistry dr = new DictionaryRegistry("../dict-data/ru");
        final FioUdf fu = new FioUdf(dr, "zztop");
        Assert.assertEquals("михайлов лолий амантиевич", fu.get(true, 7000000L));
        Assert.assertEquals("михайлова устинья амантиевна", fu.get(false, 7000000L));
    }

}
