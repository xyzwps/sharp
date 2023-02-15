package run.antleg.sharp.config.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Function;

import lombok.val;

public class LongRecordValueSerialization<V> {

    private final Function<Long, V> constructor;

    private final Function<V, Long> getValue;

    private final Class<V> classV;

    public LongRecordValueSerialization(Class<V> classV, Function<Long, V> construct, Function<V, Long> getValue) {
        this.constructor = Objects.requireNonNull(construct);
        this.getValue = Objects.requireNonNull(getValue);
        this.classV = Objects.requireNonNull(classV);
    }

    public void install(SimpleModule module) {
        module.addSerializer(this.classV, this.getSerializer());
        module.addDeserializer(this.classV, this.getDeserializer());
        module.addKeySerializer(this.classV, this.getKeySerializer());
        module.addKeyDeserializer(this.classV, this.getKeyDeserializer());
    }


    public StdDeserializer<V> getDeserializer() {
        val self = this;
        return new StdDeserializer<>(classV) {
            @SuppressWarnings("unchecked")
            @Override
            public V deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
                if (p.isExpectedNumberIntToken()) {
                    return self.constructor.apply(p.getLongValue());
                } else if (p.hasToken(JsonToken.VALUE_STRING)) {
                    return self.constructor.apply(Long.parseLong(p.getText()));
                } else {
                    return (V) ctx.handleUnexpectedToken(getValueType(ctx), p);
                }
            }
        };
    }

    public StdSerializer<V> getSerializer() {
        val self = this;
        return new StdSerializer<>(classV) {
            @Override
            public void serialize(V value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                if (value == null) {
                    gen.writeNull();
                } else {
                    gen.writeNumber(self.getValue.apply(value));
                }
            }
        };
    }

    public StdSerializer<V> getKeySerializer() {
        val self = this;
        return new StdSerializer<>(classV) {
            @Override
            public void serialize(V value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                if (value == null) {
                    throw new IllegalStateException(classV.getCanonicalName() + " Key CANNOT BE null.");
                } else {
                    gen.writeFieldName(self.getValue.apply(value) + "");
                }
            }
        };
    }

    public KeyDeserializer getKeyDeserializer() {
        val self = this;
        return new KeyDeserializer() {
            @Override
            public Object deserializeKey(String key, DeserializationContext ctx) {
                return self.constructor.apply(Long.parseLong(key));
            }
        };
    }

}
