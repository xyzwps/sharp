package run.antleg.sharp.routes;

import org.springdoc.webmvc.core.fn.SpringdocRouteBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicate;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import run.antleg.sharp.modules.OK;

import static org.springdoc.webmvc.core.fn.SpringdocRouteBuilder.route;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.servlet.function.RequestPredicates.accept;

@Configuration
public class FnRouter {

    private static final RequestPredicate ACCEPT_JSON = accept(APPLICATION_JSON);

    @Bean
    public RouterFunction<ServerResponse> routes(FnAnthologyApis anthologyApis, FnSecuritySecurityApis securityApiDoc) {
        return add(anthologyApis, securityApiDoc)
                .GET("/api/demo", ACCEPT_JSON, (request) -> ServerResponse.ok().body(OK.INSTANCE), ops -> ops
                        .operationId("demo-get").summary("demo").tag("Demo")
                        .response(responseBuilder().responseCode("200").description("成功")
                                .content(contentBuilder().schema(schemaBuilder().implementation(OK.class))))
                        .build())

                .build();
    }

    private static SpringdocRouteBuilder add(FnApis... apis) {
        var builder = route();
        for (var it : apis) {
            builder = it.add(builder);
        }
        return builder;
    }
}
