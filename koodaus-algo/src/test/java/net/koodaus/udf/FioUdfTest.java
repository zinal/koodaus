package net.koodaus.udf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author mzinal
 */
public class FioUdfTest {

    @Test
    public void test1() throws Exception {
        final long position = 7000000L;
        final FioUdf fu1 = new FioUdf("../dict-data/ru", "zztop");
        String orig1 = fu1.call(true, position);
        Assert.assertEquals("евстигнеев евсевий арсениевич", orig1);
        String orig2 = fu1.call(false, position);
        Assert.assertEquals("евстигнеева маина арсениевна", orig2);

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(fu1);
            oos.flush();
        }

        final FioUdf fu2;
        final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            fu2 = (FioUdf) ois.readObject();
        }

        String test1 = fu2.call(true, position);
        Assert.assertEquals(orig1, test1);

        String test2 = fu2.call(false, position);
        Assert.assertEquals(orig2, test2);
    }

}
