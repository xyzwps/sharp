package run.antleg.sharp.routes;

import org.springdoc.webmvc.core.fn.SpringdocRouteBuilder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import run.antleg.sharp.config.security.LoginPayload;
import run.antleg.sharp.modules.user.model.User;

import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;


/**
 * 这些 api 是 spring security 拦截处理的，这里写一点是为了生成文档。
 */
@Component
public class FnSecuritySecurityApis extends FnApis {

    public FnSecuritySecurityApis(Validator validator) {
        super(validator);
    }

    @Override
    public SpringdocRouteBuilder add(SpringdocRouteBuilder route) {
        return route
                .POST("/api/login", ACCEPT_JSON, this::impossible, opts -> opts
                        .operationId("login").summary("登录").tag("Action")
                        .requestBody(requestBodyBuilder()
                                .content(contentBuilder()
                                        .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                        .schema(schemaBuilder().implementation(LoginPayload.class))))
                        .response(responseBuilder()
                                .responseCode("200")
                                .description("登录成功")
                                .content(contentBuilder().schema(schemaBuilder().implementation(User.class))))
                        .build());
    }

    private ServerResponse impossible(ServerRequest serverRequest) {
        throw new IllegalStateException("是假的啦");
    }
}
