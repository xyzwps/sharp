package run.antleg.sharp.routes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.RequestPredicate;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import run.antleg.sharp.modules.anthology.AnthologyHandlers;
import run.antleg.sharp.modules.anthology.command.CreateAnthologyCommand;
import run.antleg.sharp.modules.anthology.model.Anthology;

import static org.springframework.web.servlet.function.RouterFunctions.*;
import static org.springframework.web.servlet.function.RequestPredicates.*;
import static org.springframework.http.MediaType.*;

@Configuration
public class AnthologyRouter {

    private static final RequestPredicate ACCEPT_JSON = accept(APPLICATION_JSON);

    @Tag(name = "文集") // TODO: not working
    @RouterOperations({
            @RouterOperation(path = "/api/anthologies", method = RequestMethod.POST, consumes = "application/json", operation = @Operation(
                    operationId = "create-anthology", summary = "创建文集",
                    requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = CreateAnthologyCommand.class))),
                    responses = {@ApiResponse(responseCode = "200", description = "创建成功", content = @Content(schema = @Schema(implementation = Anthology.class)))}
            ))
    })
    @Bean
    public RouterFunction<ServerResponse> anthologyRouterFunction(AnthologyHandlers anthologyHandlers) {
        return route()
                .POST("/api/anthologies", ACCEPT_JSON, this.createAnthology(anthologyHandlers))
                .onError(Throwable.class, HandleExceptions::handle)
                .build();
    }


    @Autowired
    private Validator validator;

    private HandlerFunction<ServerResponse> createAnthology(AnthologyHandlers anthologyHandlers) {
        return (request) -> {
            var cmd = request.body(CreateAnthologyCommand.class);
            var br = new DirectFieldBindingResult(cmd, cmd.getClass().getCanonicalName());
            validator.validate(cmd, br);
            if (br.hasErrors()) {
                throw new BindException(br); // TODO: 太复杂了
            }
            var anthology = anthologyHandlers.create(cmd);
            return ServerResponse.ok().contentType(APPLICATION_JSON).body(anthology);
        };
    }

}
