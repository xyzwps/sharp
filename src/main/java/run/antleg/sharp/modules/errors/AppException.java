package run.antleg.sharp.modules.errors;

import java.util.Objects;

public class AppException extends RuntimeException {

    public final Errors error;

    public final String description;

    public AppException(Errors errors) {
        this(errors, null);
    }

    public AppException(Errors errors, String description) {
        this.error = Objects.requireNonNull(errors);
        this.description = description;
    }

}
