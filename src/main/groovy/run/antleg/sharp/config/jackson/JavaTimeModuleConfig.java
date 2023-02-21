package run.antleg.sharp.config.jackson;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import run.antleg.sharp.util.DateUtils;

import java.time.LocalDateTime;

public final class JavaTimeModuleConfig {

    public static JavaTimeModule get() {
        var javaTimeModule = new JavaTimeModule();

        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateUtils.dateTimeFormatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateUtils.dateTimeFormatter));
        javaTimeModule.addKeyDeserializer(LocalDateTime.class, new KeyDeserializer() {
            @Override
            public LocalDateTime deserializeKey(String key, DeserializationContext ctx) {
                return LocalDateTime.parse(key, DateUtils.dateTimeFormatter);
            }
        });

        return javaTimeModule;
    }
}
