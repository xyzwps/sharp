package run.antleg.sharp.routes

import groovy.util.logging.Slf4j
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.util.Pair
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.support.WebExchangeBindException
import run.antleg.sharp.modules.errors.AppException
import run.antleg.sharp.modules.errors.Errors
import run.antleg.sharp.util.Servlets

import static run.antleg.sharp.modules.errors.Errors.*

@Slf4j
class HandleExceptions {

    static Pair<HttpStatus, ErrorBody> handle(Throwable t) {
        log.error("Request Error", t)
        return switch (t) {
            case MethodArgumentNotValidException, BindException, WebExchangeBindException -> wrap(REQUEST_INVALID, t.getFieldErrors().first()?.getDefaultMessage() ?: "请求错误")
            case AppException -> wrap(t.error)
            case AuthenticationServiceException -> {
                def cause = t.cause
                (cause instanceof BindException) ? handle(cause) : wrap(REQUEST_UNAUTHORIZED)
            }
            case BadCredentialsException -> wrap(REQUEST_UNAUTHORIZED)
            case UsernameNotFoundException -> wrap(REQUEST_UNAUTHORIZED, USER_NOT_FOUND.message)
            case AccessDeniedException -> wrap(REQUEST_FORBIDDEN)
            default -> wrap(UNHANDLED) // TODO: i18n
        }
    }

    static Pair<HttpStatus, ErrorBody> wrap(Errors error) {
        return Pair.of(error.status, new ErrorBody(msg: error.message, code: error.name()))
    }

    static Pair<HttpStatus, ErrorBody> wrap(Errors error, String message) {
        return Pair.of(error.status, new ErrorBody(msg: message, code: error.name()))
    }

    static void write(Throwable t, HttpServletResponse response) {
        var pair = handle(t)
        Servlets.sendJson(response, pair.first, pair.second)
    }

    static class ErrorBody {
        String msg
        String code
    }
}
