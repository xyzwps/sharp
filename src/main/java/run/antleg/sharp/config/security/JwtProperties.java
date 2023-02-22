package run.antleg.sharp.config.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * TODO: 这个配置类是不是可以改成接口？
 */
@ConfigurationProperties(prefix = "app.jwt")
@Data
public class JwtProperties {
    private String hs512Key;
}
