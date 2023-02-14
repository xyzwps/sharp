package run.antleg.sharp.config.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import run.antleg.sharp.modules.user.model.UserId;

import java.io.IOException;

public class UserIdSerializer extends StdSerializer<UserId> {

    public static final UserIdSerializer INSTANCE = new UserIdSerializer();

    protected UserIdSerializer() {
        super(UserId.class);
    }

    @Override
    public void serialize(UserId value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            gen.writeNumber(value.value());
        }
    }
}
