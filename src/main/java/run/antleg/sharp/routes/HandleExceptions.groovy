package run.antleg.sharp.routes

import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.util.Pair
import org.springframework.http.HttpStatus
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

class HandleExceptions {

    static Pair<HttpStatus, ErrorBody> handle(Throwable t) {
        return switch (t) {
            case MethodArgumentNotValidException, BindException, WebExchangeBindException -> Pair.of(
                    REQUEST_INVALID.status,
                    new ErrorBody(msg: t.getFieldErrors().first()?.getDefaultMessage() ?: "请求错误", code: REQUEST_INVALID.name())
            )
            case AppException -> wrap(t.error)
            case AuthenticationServiceException -> {
                def cause = t.cause
                (cause instanceof BindException) ? handle(cause) : wrap(REQUEST_UNAUTHORIZED)
            }
            case BadCredentialsException -> wrap(REQUEST_UNAUTHORIZED)
            case UsernameNotFoundException -> wrap(REQUEST_UNAUTHORIZED, USER_NOT_FOUND.message)
            default -> Pair.of(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    new ErrorBody(msg: t.getMessage()) // TODO:
            )
        }
    }

    static Pair<HttpStatus, ErrorBody> wrap(Errors error) {
        return Pair.of(error.status, new ErrorBody(msg: error.message, code: error.status))
    }

    static Pair<HttpStatus, ErrorBody> wrap(Errors error, String message) {
        return Pair.of(error.status, new ErrorBody(msg: message, code: error.status))
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
