package run.antleg.sharp.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "openapi")
class OpenApiProperties {

    Server server

    static class Server {
        String url
        String description
    }
}
