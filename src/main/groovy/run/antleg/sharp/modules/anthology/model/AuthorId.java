package run.antleg.sharp.modules.anthology.model;

import io.swagger.v3.oas.annotations.media.Schema;
import run.antleg.sharp.modules.user.model.UserId;

import java.io.Serializable;

@Schema(type = "integer", format = "int64")
public record AuthorId(Long value) implements Serializable {

    public static AuthorId from(UserId userId) {
        return new AuthorId(userId.value());
    }
}
