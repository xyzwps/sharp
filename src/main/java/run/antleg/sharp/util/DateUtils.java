package run.antleg.sharp.util;

import java.time.format.DateTimeFormatter;

public final class DateUtils {

    public static final String dateTimeFormatterPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormatterPattern);
}
