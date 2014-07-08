package co.cryptocorp.oracle.core;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
* @author devrandom
*/
public class DateSerializer extends com.fasterxml.jackson.databind.ser.std.DateSerializer {
    private static final SimpleDateFormat DATE_FORMAT;

    static {
        DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public DateSerializer() {
        super(false, DATE_FORMAT);
    }
}
