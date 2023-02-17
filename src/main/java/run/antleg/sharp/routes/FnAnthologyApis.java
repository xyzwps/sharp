package run.antleg.sharp.routes;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springdoc.webmvc.core.fn.SpringdocRouteBuilder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.*;
import org.springframework.web.servlet.function.*;
import run.antleg.sharp.modules.OK;
import run.antleg.sharp.modules.anthology.AnthologyHandlers;
import run.antleg.sharp.modules.anthology.command.UpsertAnthologyCommand;
import run.antleg.sharp.modules.anthology.model.Anthology;
import run.antleg.sharp.modules.anthology.model.AnthologyId;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;

@Component
public class FnAnthologyApis extends FnApis {

    private final AnthologyHandlers anthologyHandlers;

    public FnAnthologyApis(Validator validator, AnthologyHandlers anthologyHandlers) {
        super(validator);
        this.anthologyHandlers = anthologyHandlers;
    }


    private ServerResponse createAnthology(ServerRequest request) {
        var cmd = getAndValidateBody(request, UpsertAnthologyCommand.class);
        var anthology = anthologyHandlers.create(cmd);
        return okJson(anthology);
    }

    private ServerResponse updateAnthology(ServerRequest request) {
        var cmd = getAndValidateBody(request, UpsertAnthologyCommand.class);
        var anthologyId = new AnthologyId(request.pathVariable("anthologyId"));
        var anthology = anthologyHandlers.update(anthologyId, cmd);
        return okJson(anthology);
    }

    private ServerResponse getAnthology(ServerRequest request) {
        var anthologyId = new AnthologyId(request.pathVariable("anthologyId"));
        return okJson(anthologyHandlers.get(anthologyId));
    }

    private ServerResponse deleteAnthology(ServerRequest request) {
        var anthologyId = new AnthologyId(request.pathVariable("anthologyId"));
        anthologyHandlers.delete(anthologyId);
        return okJson(OK.INSTANCE);
    }

    @Override
    public SpringdocRouteBuilder add(SpringdocRouteBuilder route) {
        return route
                .POST("/api/anthologies", ACCEPT_JSON, this::createAnthology, opts -> opts
                        .operationId("create-anthology").summary("创建文集").tag("文集")
                        .requestBody(requestBodyBuilder()
                                .content(contentBuilder()
                                        .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                        .schema(schemaBuilder().implementation(UpsertAnthologyCommand.class))))
                        .response(responseBuilder()
                                .responseCode("200")
                                .description("创建成功")
                                .content(contentBuilder().schema(schemaBuilder().implementation(Anthology.class))))
                        .build())
                .PATCH("/api/anthologies/{anthologyId}", ACCEPT_JSON, this::updateAnthology, opts -> opts
                        .operationId("update-anthology").summary("更新文集").tag("文集")
                        .parameter(parameterBuilder().name("anthologyId").description("文集 ID").in(ParameterIn.PATH))
                        .requestBody(requestBodyBuilder()
                                .content(contentBuilder()
                                        .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                        .schema(schemaBuilder().implementation(UpsertAnthologyCommand.class))))
                        .response(responseBuilder()
                                .responseCode("200")
                                .description("更新成功")
                                .content(contentBuilder().schema(schemaBuilder().implementation(Anthology.class))))
                        .build())
                .DELETE("/api/anthologies/{anthologyId}", ACCEPT_JSON, this::deleteAnthology, opts -> opts
                        .operationId("delete-an-anthology").summary("删除一个文集").tag("文集")
                        .parameter(parameterBuilder().name("anthologyId").description("文集 ID").in(ParameterIn.PATH))
                        .response(responseBuilder()
                                .responseCode("200")
                                .description("删除成功")
                                .content(contentBuilder().schema(schemaBuilder().implementation(OK.class))))
                        .build())
                .GET("/api/anthologies/{anthologyId}", ACCEPT_JSON, this::getAnthology, opts -> opts
                        .operationId("get-an-anthology").summary("查询一个文集").tag("文集")
                        .parameter(parameterBuilder().name("anthologyId").description("文集 ID").in(ParameterIn.PATH))
                        .response(responseBuilder()
                                .responseCode("200")
                                .description("查到")
                                .content(contentBuilder().schema(schemaBuilder().implementation(Anthology.class))))
                        .build());
    }

}
