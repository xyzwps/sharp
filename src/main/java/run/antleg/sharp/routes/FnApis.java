package run.antleg.sharp.routes;

import org.springdoc.webmvc.core.fn.SpringdocRouteBuilder;
import org.springframework.validation.BindException;
import org.springframework.validation.DirectFieldBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.function.RequestPredicate;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.servlet.function.RequestPredicates.accept;

public abstract class FnApis {
    protected static final RequestPredicate ACCEPT_JSON = accept(APPLICATION_JSON);

    protected final Validator validator;

    protected FnApis(Validator validator) {
        this.validator = validator;
    }

    public abstract SpringdocRouteBuilder add(SpringdocRouteBuilder route);

    protected ServerResponse okJson(Object body) {
        return ServerResponse.ok().contentType(APPLICATION_JSON).body(body);
    }

    protected <T> T getAndValidateBody(ServerRequest request, Class<T> tClass) {
        try {
            var cmd = request.body(tClass);
            var br = new DirectFieldBindingResult(cmd, cmd.getClass().getCanonicalName());
            validator.validate(cmd, br);
            if (br.hasErrors()) {
                throw new BindException(br);
            }
            return cmd;
        } catch (Exception ex) {
            throw new RuntimeException(ex);  // TODO: 处理异常
        }
    }
}
