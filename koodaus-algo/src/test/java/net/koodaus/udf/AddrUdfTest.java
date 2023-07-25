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
public class AddrUdfTest {

    @Test
    public void test1() throws Exception {
        final long position = 7000000L;
        final AddrUdf fu1 = new AddrUdf("../dict-data/test", "zztop");
        String city1 = fu1.getCity(position);
        // System.out.println(city1.toLowerCase());
        Assert.assertEquals("сеймчан", city1.toLowerCase());
        String addr1 = fu1.getStreet(position);
        //System.out.println(addr1.toLowerCase());
        Assert.assertEquals("двадцати шести бакинских комиссаров улица", addr1.toLowerCase());

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(fu1);
            oos.flush();
        }

        final AddrUdf fu2;
        final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            fu2 = (AddrUdf) ois.readObject();
        }

        String city2 = fu2.getCity(position);
        Assert.assertEquals(city1, city2);

        String addr2 = fu2.getStreet(position);
        Assert.assertEquals(addr1, addr2);
    }

}
