package run.antleg.sharp.routes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springdoc.webmvc.core.fn.SpringdocRouteBuilder.route;

@Configuration
public class FnRouter {

    @Bean
    public RouterFunction<ServerResponse> routes(FnSecuritySecurityApis securityApiDoc) {
        return securityApiDoc.add(route()).build();
    }
}
