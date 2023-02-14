package run.antleg.sharp.config.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import run.antleg.sharp.modules.user.model.UserId;

import java.io.IOException;

public class UserIdDeserializer extends StdDeserializer<UserId> {

    public static final UserIdDeserializer INSTANCE = new UserIdDeserializer();

    protected UserIdDeserializer() {
        super(UserId.class);
    }

    @Override
    public UserId deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        if (p.isExpectedNumberIntToken()) {
            return new UserId(p.getLongValue());
        } else if (p.hasToken(JsonToken.VALUE_STRING)) {
            return new UserId(Long.parseLong(p.getText()));
        } else {
            return (UserId) ctx.handleUnexpectedToken(getValueType(ctx), p);
        }
    }
}
