package run.antleg.sharp.modules.errors;

import org.springframework.http.HttpStatus;

import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

public enum Errors {
    ANTHOLOGY_NOT_FOUND("找不到文集", NOT_FOUND),

    INVALID_REQUEST("请求错误", BAD_REQUEST),
    ;

    Errors(String message, HttpStatus status) {
        this.message = Objects.requireNonNull(message);
        this.status = Objects.requireNonNull(status);
    }

    public final String message;

    public final HttpStatus status;
}
