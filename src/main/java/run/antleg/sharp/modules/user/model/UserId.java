package run.antleg.sharp.modules.user.model;

import java.io.Serializable;

public record UserId(Long value) implements Serializable {

    @Override
    public String toString() {
        return value().toString();
    }

    public static UserId from(String value) {
        return new UserId(Long.parseLong(value));
    }
}