package run.antleg.sharp.config.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import run.antleg.sharp.modules.user.model.UserId;

import java.io.IOException;

public class UserIdKeySerializer extends StdSerializer<UserId> {

    public static final UserIdKeySerializer INSTANCE = new UserIdKeySerializer();

    protected UserIdKeySerializer() {
        super(UserId.class);
    }

    @Override
    public void serialize(UserId value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value == null) {
            throw new IllegalStateException(UserId.class.getCanonicalName() + " Key CANNOT BE null.");
        } else {
            gen.writeFieldName(value.value() + "");
        }
    }
}
