package run.antleg.sharp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.openapi")
public class OpenApiProperties {

    private Server server;

    @Data
    public static class Server {
        String url;
        String description;
    }
}
