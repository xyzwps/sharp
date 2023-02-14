package run.antleg.sharp.config.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import run.antleg.sharp.modules.user.model.UserId;

public class SharpModule extends SimpleModule {
    public SharpModule() {
        addDeserializer(UserId.class, UserIdDeserializer.INSTANCE);

        addSerializer(UserId.class, UserIdSerializer.INSTANCE);

        addKeySerializer(UserId.class, UserIdKeySerializer.INSTANCE);

        addKeyDeserializer(UserId.class, UserIdKeyDeserializer.INSTANCE);
    }
}
