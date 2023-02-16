package run.antleg.sharp.modules.errors;

import java.util.Objects;

public class AppException extends RuntimeException {

    public final Errors errors;

    public final String description;

    public AppException(Errors errors) {
        this(errors, null);
    }

    public AppException(Errors errors, String description) {
        this.errors = Objects.requireNonNull(errors);
        this.description = description;
    }

}
