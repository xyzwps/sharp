package run.antleg.sharp.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.StringSchema
import io.swagger.v3.oas.models.parameters.HeaderParameter
import io.swagger.v3.oas.models.servers.Server
import org.springdoc.core.customizers.GlobalOpenApiCustomizer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import run.antleg.sharp.util.JSON

@Configuration
class Beans {

    @Bean
    OpenAPI openApi(OpenApiProperties properties,
                    @Value('${server.port}') Integer port) {
        return new OpenAPI()
                .addServersItem(new Server()
                        .url(properties.server?.url ?: "http://localhost:${port}")
                        .description(properties.server?.description ?: "localhost"))
                .info(new Info()
                        .title("Sharp API")
                        .description("Sharp API Documents"))
    }


    @Bean
    GlobalOpenApiCustomizer globalOpenApiCustomizer() {
        return { openApi ->

            var commonHeader = new HeaderParameter()
                    .name("X-Request-Id")
                    .schema(new StringSchema())
                    .description("请求 ID，用于在日志中追踪请求。你可以使用 UUID 等算法生成") // TODO: 真的生成 request-id
                    .example("i-am-a-cool-id")

            openApi.paths.values().stream()
                    .flatMap { it.readOperations().stream() }
                    .forEach { it.addParametersItem(commonHeader) }
        }
    }


    @Bean
    ObjectMapper objectMapper() {
        return JSON.OBJECT_MAPPER
    }
}
