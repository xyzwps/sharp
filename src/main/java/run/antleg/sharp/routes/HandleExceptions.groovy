package run.antleg.sharp.routes

import org.springframework.data.util.Pair
import org.springframework.http.HttpStatus
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

import static org.springframework.http.MediaType.APPLICATION_JSON

class HandleExceptions {

    static Pair<HttpStatus, ErrorBody> handle(Throwable t) {
        return switch (t) {
            case MethodArgumentNotValidException -> Pair.of(
                    HttpStatus.BAD_REQUEST,
                    new ErrorBody(msg: t.getFieldErrors().first()?.getDefaultMessage() ?: "请求错误")
            )
            case BindException -> Pair.of(
                    HttpStatus.BAD_REQUEST,
                    new ErrorBody(msg: t.getFieldErrors().first()?.getDefaultMessage() ?: "请求错误")
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
    }
}
