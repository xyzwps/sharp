package run.antleg.sharp.util;

import java.time.format.DateTimeFormatter;

public final class DateUtils {

    public static final String dateTimeFormatterPattern = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormatterPattern);
}
