package run.antleg.sharp.routes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.ServletException;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.function.*;
import run.antleg.sharp.modules.OK;
import run.antleg.sharp.modules.anthology.AnthologyHandlers;
import run.antleg.sharp.modules.anthology.command.UpsertAnthologyCommand;
import run.antleg.sharp.modules.anthology.model.Anthology;
import run.antleg.sharp.modules.anthology.model.AnthologyId;

import java.io.IOException;

import static org.springframework.web.servlet.function.RouterFunctions.*;
import static org.springframework.web.servlet.function.RequestPredicates.*;
import static org.springframework.http.MediaType.*;

@Configuration
public class AnthologyRouter {

    private static final RequestPredicate ACCEPT_JSON = accept(APPLICATION_JSON);

    @RouterOperations({
            @RouterOperation(path = "/api/anthologies", method = RequestMethod.POST, consumes = "application/json", operation = @Operation(
                    operationId = "create-anthology", summary = "创建文集", tags = "文集",
                    requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = UpsertAnthologyCommand.class))),
                    responses = @ApiResponse(responseCode = "200", description = "创建成功", content = @Content(schema = @Schema(implementation = Anthology.class)))
            )),
            @RouterOperation(path = "/api/anthologies/{anthologyId}", method = RequestMethod.PUT, consumes = "application/json", operation = @Operation(
                    operationId = "update-anthology", summary = "修改文集", tags = "文集",
                    parameters = @Parameter(name = "anthologyId", in = ParameterIn.PATH, description = "文集 ID", required = true),
                    requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = UpsertAnthologyCommand.class))),
                    responses = @ApiResponse(responseCode = "200", description = "修改成功", content = @Content(schema = @Schema(implementation = Anthology.class)))
            )),
            @RouterOperation(path = "/api/anthologies/{anthologyId}", method = RequestMethod.GET, consumes = "application/json", operation = @Operation(
                    operationId = "get-anthology", summary = "获取单个文集", tags = "文集",
                    parameters = @Parameter(name = "anthologyId", in = ParameterIn.PATH, description = "文集 ID", required = true),
                    responses = @ApiResponse(responseCode = "200", description = "获取成功", content = @Content(schema = @Schema(implementation = Anthology.class)))
            )),
            @RouterOperation(path = "/api/anthologies/{anthologyId}", method = RequestMethod.DELETE, consumes = "application/json", operation = @Operation(
                    operationId = "delete-anthology", summary = "删除文集", tags = "文集",
                    parameters = @Parameter(name = "anthologyId", in = ParameterIn.PATH, description = "文集 ID", required = true),
                    responses = @ApiResponse(responseCode = "200", description = "删除成功", content = @Content(schema = @Schema(implementation = OK.class)))
            ))
    })
    @Bean
    public RouterFunction<ServerResponse> anthologyRouterFunction(AnthologyHandlers anthologyHandlers) {
        return route()
                .POST("/api/anthologies", ACCEPT_JSON, this.createAnthology(anthologyHandlers))
                .PUT("/api/anthologies/{anthologyId}", ACCEPT_JSON, this.updateAnthology(anthologyHandlers))
                .GET("/api/anthologies/{anthologyId}", ACCEPT_JSON, this.getAnthology(anthologyHandlers))
                .DELETE("/api/anthologies/{anthologyId}", ACCEPT_JSON, this.deleteAnthology(anthologyHandlers))
                .onError(Throwable.class, HandleExceptions::handle)
                .build();
    }


    @Autowired
    private Validator validator;

    private HandlerFunction<ServerResponse> createAnthology(AnthologyHandlers anthologyHandlers) {
        return (request) -> {
            var cmd = getBody(request, UpsertAnthologyCommand.class);
            var anthology = anthologyHandlers.create(cmd);
            return okJson(anthology);
        };
    }

    private HandlerFunction<ServerResponse> updateAnthology(AnthologyHandlers anthologyHandlers) {
        return (request) -> {
            var cmd = getBody(request, UpsertAnthologyCommand.class);
            var anthologyId = new AnthologyId(request.pathVariable("anthologyId"));
            var anthology = anthologyHandlers.update(anthologyId, cmd);
            return okJson(anthology);
        };
    }

    private HandlerFunction<ServerResponse> getAnthology(AnthologyHandlers anthologyHandlers) {
        return (request) -> {
            var anthologyId = new AnthologyId(request.pathVariable("anthologyId"));
            return okJson(anthologyHandlers.get(anthologyId));
        };
    }

    private HandlerFunction<ServerResponse> deleteAnthology(AnthologyHandlers anthologyHandlers) {
        return (request) -> {
            var anthologyId = new AnthologyId(request.pathVariable("anthologyId"));
            anthologyHandlers.delete(anthologyId);
            return okJson(OK.INSTANCE);
        };
    }

    private ServerResponse okJson(Object body) {
        return ServerResponse.ok().contentType(APPLICATION_JSON).body(body);
    }

    private <T> T getBody(ServerRequest request, Class<T> tClass) throws ServletException, IOException, BindException {
        var cmd = request.body(tClass);
        var br = new DirectFieldBindingResult(cmd, cmd.getClass().getCanonicalName());
        validator.validate(cmd, br);
        if (br.hasErrors()) {
            throw new BindException(br);
        }
        return cmd;
    }
}
