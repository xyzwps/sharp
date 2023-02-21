package run.antleg.sharp.modules.errors;

import org.springframework.http.HttpStatus;

import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

public enum Errors {
    USER_NOT_FOUND("用户不存在", NOT_FOUND),

    USERNAME_CONFLICT("用户名已存在", NOT_FOUND),

    ANTHOLOGY_NOT_FOUND("找不到文集", NOT_FOUND),

    TODO_NOT_FOUND("找不到待办", NOT_FOUND),

    POST_NOT_FOUND("找不到文章", NOT_FOUND),

    TAGGED_TYPE_UNSUPPORTED("暂不支持的可打标签的资源", BAD_REQUEST),

    REQUEST_INVALID("请求错误", BAD_REQUEST),
    REQUEST_UNAUTHORIZED("尚未登录", UNAUTHORIZED),

    REQUEST_FORBIDDEN("未授权", FORBIDDEN),
    UNHANDLED("未处理的错误", INTERNAL_SERVER_ERROR),
    IMPOSSIBLE("不可能的情况", INTERNAL_SERVER_ERROR)
    ;

    Errors(String message, HttpStatus status) {
        this.message = Objects.requireNonNull(message);
        this.status = Objects.requireNonNull(status);
    }

    public final String message;

    public final HttpStatus status;
}
