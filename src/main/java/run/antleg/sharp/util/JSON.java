package run.antleg.sharp.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import run.antleg.sharp.config.jackson.SharpModule;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public class JSON {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.registerModules(new SharpModule(), new JavaTimeModule());
    }


    public static <T> T parse(InputStream is, Class<T> tClass) {
        try {
            return OBJECT_MAPPER.readValue(is, tClass);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
