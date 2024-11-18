package net.koodaus.udf;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.SplittableRandom;
import net.koodaus.algo.PureJavaCrc64;

/**
 *
 * @author mzinal
 */
public class DateUdf implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final long keyState;
    private final LocalDate start;
    private final LocalDate finish;
    private final int days;

    public DateUdf(byte[] userKey, long subkey, LocalDate start, LocalDate finish) {
        if (userKey==null || userKey.length==0) {
            keyState = subkey;
        } else {
            keyState = PureJavaCrc64.update(subkey, userKey, 0, userKey.length);
        }
        this.start = start;
        this.finish = finish;
        this.days = (int) ( ChronoUnit.DAYS.between(start, finish) + 1 );
    }

    public DateUdf(String userKey, long subkey, LocalDate start, LocalDate finish) {
        this((userKey==null || userKey.length()==0) ?
                (byte[])null : userKey.getBytes(StandardCharsets.UTF_8),
                subkey, start, finish);
    }

    public DateUdf(String userKey, LocalDate start, LocalDate finish) {
        this((userKey==null || userKey.length()==0) ?
                (byte[])null : userKey.getBytes(StandardCharsets.UTF_8),
                0L, start, finish);
    }

    public DateUdf(byte[] userKey, LocalDate start, LocalDate finish) {
        this(userKey, 0L, start, finish);
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getFinish() {
        return finish;
    }

    public int getDays() {
        return days;
    }

    public LocalDate nextDate(long position) {
        final SplittableRandom random = new SplittableRandom(
                PureJavaCrc64.update(keyState, position));
        return start.plusDays(random.nextInt(days));
    }
    
    public String nextString(long position) {
        return nextDate(position).toString();
    }

}
