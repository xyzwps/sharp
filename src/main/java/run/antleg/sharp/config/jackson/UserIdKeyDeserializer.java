package run.antleg.sharp.config.jackson;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import run.antleg.sharp.modules.user.model.UserId;

public class UserIdKeyDeserializer extends KeyDeserializer {

    public static final UserIdKeyDeserializer INSTANCE = new UserIdKeyDeserializer();

    @Override
    public Object deserializeKey(String key, DeserializationContext ctx) {
        return new UserId(Long.parseLong(key));
    }
}
