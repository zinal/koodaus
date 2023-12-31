package net.koodaus.udf;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author mzinal
 */
public class UtilUdfTest {

    private static final String PASSWORD = "служил гаврило колумнистом";
    private static final String FIO_FIRST = "Гагик Спартакович";
    private static final String FIO_LAST = "Пагосян";
    private static final String FIO_FULL = "Пагосян Гагик Спартакович";

    private static final String[] VALUES = {
        "uvI53uYUDX4AAAAAAAAAAQ",
        "sbI53uYUDX4AAAAAAAAADQ",
        "WXI53uYUDX4AAAAAAAAAqQ",
        "fz-53uYUDX4AAAAAAAAIlQ",
        "eaGp3uYUDX4AAAAAAABvkQ",
        "xlLeruYUDX4AAAAAAAWqXQ",
        "Qpn57uYUDX4AAAAAAEmmuQ",
        "5nfWLDYUDX4AAAAAA713ZQ",
        "jOnzY-YUDX4AAAAAMJ8QIQ",
        "XxyQQmV0DX4AAAACeBPRrQ",
        "A5tIeeAUDX4AAAAgGQGlyQ",
        "kWd1xXmlvX4AAAGhRRVrNQ",
        "T7KXpasIfX4AABUwghZxsQ",
        "KAF9wvKt3M4AARN2myPF_Q"
    };

    @Test
    public void testLong2Str() {
        final UtilUdf uu = new UtilUdf(PASSWORD);
        int pos = 0;
        for (long v = 1; v < 999999999999999L; v *= 13L) {
            String xv = uu.long2str(v);
            Assert.assertEquals(VALUES[pos], xv);
            pos += 1;
        }
    }

    @Test
    public void testFirstname() {
        final UtilUdf uu = new UtilUdf(PASSWORD);
        String v = uu.firstname(FIO_FULL);
        Assert.assertEquals(FIO_FIRST, v);
    }

    @Test
    public void testLastname() {
        final UtilUdf uu = new UtilUdf(PASSWORD);
        String v = uu.lastname(FIO_FULL);
        Assert.assertEquals(FIO_LAST, v);
    }

}
