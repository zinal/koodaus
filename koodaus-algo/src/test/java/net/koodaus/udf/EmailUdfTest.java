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
public class EmailUdfTest {

    @Test
    public void test1() throws Exception {
        final long position = 7000000L;
        final EmailUdf fu1 = new EmailUdf("../dict-data/test", "zztop");
        String email1 = fu1.getEmail(position);
        //System.out.println(email1.toLowerCase());
        Assert.assertEquals("joga@gmail.com", email1.toLowerCase());

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(fu1);
            oos.flush();
        }

        final EmailUdf fu2;
        final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            fu2 = (EmailUdf) ois.readObject();
        }

        String email2 = fu2.getEmail(position);
        Assert.assertEquals(email1, email2);
    }

}
