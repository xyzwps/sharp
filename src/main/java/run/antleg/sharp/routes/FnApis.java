package run.antleg.sharp.routes;

import org.springdoc.webmvc.core.fn.SpringdocRouteBuilder;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.function.RequestPredicate;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.servlet.function.RequestPredicates.accept;

/**
 * TODO: 太不好用了，草，改了，几个 Fn*.java 浓缩成一个文件
 */
public abstract class FnApis {
    protected static final RequestPredicate ACCEPT_JSON = accept(APPLICATION_JSON);

    protected final Validator validator;

    protected FnApis(Validator validator) {
        this.validator = validator;
    }

    public abstract SpringdocRouteBuilder add(SpringdocRouteBuilder route);

}
