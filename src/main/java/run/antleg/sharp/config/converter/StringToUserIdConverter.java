package run.antleg.sharp.config.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import run.antleg.sharp.modules.user.model.UserId;

public class StringToUserIdConverter implements Converter<String, UserId> {
    @Override
    public UserId convert(@NonNull String it) {
        return new UserId(Long.parseLong(it));
    }
}
