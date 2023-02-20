package run.antleg.sharp.util;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class ThreadSafeDateFormat extends DateFormat {

    private static final ThreadLocal<SimpleDateFormat> th = new ThreadLocal<>();

    private final String pattern;

    public ThreadSafeDateFormat(String pattern) {
        this.pattern = Objects.requireNonNull(pattern);
    }

    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        return getFormatter().format(date, toAppendTo, fieldPosition);
    }

    @Override
    public Date parse(String source, ParsePosition pos) {
        return getFormatter().parse(source, pos);
    }

    private SimpleDateFormat getFormatter() {
        var formatter = th.get();
        if (formatter == null) {
            formatter = new SimpleDateFormat(pattern);
            th.set(formatter);
        }
        return formatter;
    }
}
