package run.antleg.sharp.config.security;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public final class SecurityDicts {

    public static final String JWT_COOKIE_NAME = "t";

    public static final AntPathRequestMatcher LOGIN_REQUEST_MATCHER = new AntPathRequestMatcher("/api/login", "POST");

    public static final AntPathRequestMatcher ALL_API_REQUEST_MATCHER = new AntPathRequestMatcher("/api/**");
}
