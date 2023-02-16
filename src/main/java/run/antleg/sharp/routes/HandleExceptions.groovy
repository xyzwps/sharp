package run.antleg.sharp.routes

import org.springframework.data.util.Pair
import org.springframework.http.HttpStatus
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import run.antleg.sharp.modules.errors.AppException

import static org.springframework.http.MediaType.APPLICATION_JSON
import static run.antleg.sharp.modules.errors.Errors.*

class HandleExceptions {

    static Pair<HttpStatus, ErrorBody> handle(Throwable t) {
        return switch (t) {
            case MethodArgumentNotValidException, BindException -> Pair.of(
                    INVALID_REQUEST.status,
                    new ErrorBody(msg: t.getFieldErrors().first()?.getDefaultMessage() ?: "请求错误", code: INVALID_REQUEST.name())
            )
            case AppException -> Pair.of(
                    t.errors.status,
                    new ErrorBody(msg: t.errors.message, code: t.errors.name())
            )
            default -> Pair.of(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    new ErrorBody(msg: t.getMessage())
            )
        }
    }


    static ServerResponse handle(Throwable t, ServerRequest request) {
        var pair = handle(t)
        return ServerResponse.status(pair.first).contentType(APPLICATION_JSON).body(pair.second)
    }

    static class ErrorBody {
        String msg
        String code
    }
}
