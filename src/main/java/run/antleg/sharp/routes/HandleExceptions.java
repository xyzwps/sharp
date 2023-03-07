package run.antleg.sharp.routes;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.support.WebExchangeBindException;
import run.antleg.sharp.config.servlet.MDCFilter;
import run.antleg.sharp.modules.errors.AppException;
import run.antleg.sharp.modules.errors.Errors;
import run.antleg.sharp.util.Servlets;

import java.util.List;

import static run.antleg.sharp.modules.errors.Errors.*;

@Slf4j
public class HandleExceptions {

    public static Pair<HttpStatus, ErrorBody> handle(Throwable t) {
        log.error("Request Error", t);
        return switch (t) {
            /* Bad Request Exceptions */
            case MethodArgumentNotValidException ex -> wrapFieldErrors(ex.getFieldErrors());
            case BindException ex -> wrapFieldErrors(ex.getFieldErrors());
            case WebExchangeBindException ex -> wrapFieldErrors(ex.getFieldErrors());
            /* Spring security Exceptions */
            case AuthenticationServiceException ignored -> wrap(REQUEST_UNAUTHORIZED);
            case AuthenticationCredentialsNotFoundException ignored -> wrap(REQUEST_UNAUTHORIZED);
            case BadCredentialsException ex -> wrap(REQUEST_UNAUTHORIZED, ex.getMessage());
            case UsernameNotFoundException ignored -> wrap(REQUEST_UNAUTHORIZED, USER_NOT_FOUND.message);
            case AccessDeniedException ignored -> wrap(REQUEST_FORBIDDEN);
            /* Business Exceptions */
            case AppException ex -> wrap(ex.error);
            default -> wrap(UNHANDLED); // TODO: i18n
        };
    }

    private static Pair<HttpStatus, ErrorBody> wrapFieldErrors(List<FieldError> fieldErrors) {
        var message = fieldErrors == null
                ? "请求错误(0)"
                : fieldErrors.stream().findFirst().map(DefaultMessageSourceResolvable::getDefaultMessage).orElse("请求错误");
        return wrap(REQUEST_INVALID, message);
    }

    private static Pair<HttpStatus, ErrorBody> wrap(Errors error) {
        return Pair.of(error.status, new ErrorBody(error));
    }

    private static Pair<HttpStatus, ErrorBody> wrap(Errors error, String message) {
        return Pair.of(error.status, new ErrorBody(error, message));
    }

    public static void write(Throwable t, HttpServletResponse response) {
        var pair = handle(t);
        Servlets.sendJson(response, pair.getFirst(), pair.getSecond());
    }

    @Getter
    public static class ErrorBody {
        private final String msg;
        private final String code;
        private final String requestId;

        public ErrorBody(Errors error, String msg) {
            this(error.name(), msg);
        }

        private ErrorBody(String code, String msg) {
            this.code = code;
            this.msg = msg;
            this.requestId = MDCFilter.getRequestId().orElse(null);
        }

        public ErrorBody(Errors error) {
            this(error.name(), error.message);
        }
    }
}
