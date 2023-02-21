package run.antleg.sharp.routes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RequestPredicate;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import run.antleg.sharp.config.security.LoginPayload;
import run.antleg.sharp.modules.user.model.User;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;
import static org.springdoc.webmvc.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.servlet.function.RequestPredicates.accept;

@Configuration
public class FnRouter {

    protected static final RequestPredicate ACCEPT_JSON = accept(APPLICATION_JSON);

    /**
     * @see run.antleg.sharp.config.security.RestLoginAuthenticationFilter
     */
    @Bean
    public RouterFunction<ServerResponse> routes() {
        return route()
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
                        .build())
                .build();
    }

    private ServerResponse impossible(ServerRequest serverRequest) {
        throw new IllegalStateException("是假的啦");
    }
}
