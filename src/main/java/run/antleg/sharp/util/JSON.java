package run.antleg.sharp.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import run.antleg.sharp.config.jackson.JavaTimeModuleConfig;
import run.antleg.sharp.config.jackson.SharpModule;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public class JSON {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.registerModules(new SharpModule(), JavaTimeModuleConfig.get());
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
        OBJECT_MAPPER.setDateFormat(new ThreadSafeDateFormat(DateUtils.dateTimeFormatterPattern));
    }


    public static <T> T parse(InputStream is, Class<T> tClass) {
        try {
            return OBJECT_MAPPER.readValue(is, tClass);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
