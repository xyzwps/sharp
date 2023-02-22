package run.antleg.sharp.util;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public final class DateUtils {

    public static final String dateTimeFormatterPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final Pattern dateTimeFormatterPatternRegExp = Pattern.compile("^\\d{4}-[01]\\d-[0-2]\\dT[0-2]\\d:[0-5]\\d:[0-5]\\d.\\d{3}Z$");
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormatterPattern);
}
