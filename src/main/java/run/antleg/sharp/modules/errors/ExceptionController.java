package run.antleg.sharp.modules.errors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler
    public ResponseEntity<HandleExceptions.ErrorBody> handle(Throwable t) {
        var pair = HandleExceptions.handle(t);
        return new ResponseEntity<>(pair.getSecond(), pair.getFirst());
    }
}
