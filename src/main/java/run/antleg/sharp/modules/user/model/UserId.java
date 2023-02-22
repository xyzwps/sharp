package run.antleg.sharp.modules.user.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@Schema(type = "integer", format = "int64")
public record UserId(Long value) implements Serializable {

    @Override
    public String toString() {
        return value().toString();
    }

    public static UserId from(String value) {
        return new UserId(Long.parseLong(value));
    }
}