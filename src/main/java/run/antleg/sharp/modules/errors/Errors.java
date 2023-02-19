package run.antleg.sharp.modules.errors;

import org.springframework.http.HttpStatus;

import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

public enum Errors {
    USER_NOT_FOUND("用户不存在", NOT_FOUND),

    USERNAME_CONFLICT("用户名已存在", NOT_FOUND),

    ANTHOLOGY_NOT_FOUND("找不到文集", NOT_FOUND),

    REQUEST_INVALID("请求错误", BAD_REQUEST),
    REQUEST_UNAUTHORIZED("尚未登录", UNAUTHORIZED),

    REQUEST_FORBIDDEN("未授权", FORBIDDEN),
    ;

    Errors(String message, HttpStatus status) {
        this.message = Objects.requireNonNull(message);
        this.status = Objects.requireNonNull(status);
    }

    public final String message;

    public final HttpStatus status;
}
