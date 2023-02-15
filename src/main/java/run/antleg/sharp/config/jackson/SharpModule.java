package run.antleg.sharp.config.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import run.antleg.sharp.modules.anthology.model.AnthologyId;
import run.antleg.sharp.modules.anthology.model.AuthorId;
import run.antleg.sharp.modules.user.model.UserId;

public class SharpModule extends SimpleModule {
    public SharpModule() {
        new LongRecordValueSerialization<>(UserId.class, UserId::new, UserId::value).install(this);

        new LongRecordValueSerialization<>(AuthorId.class, AuthorId::new, AuthorId::value).install(this);
        new StringRecordValueSerialization<>(AnthologyId.class, AnthologyId::new, AnthologyId::value).install(this);
    }
}
