package run.antleg.sharp.config.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
@Data
public class JwtProperties {
    private String hs512Key;
}
