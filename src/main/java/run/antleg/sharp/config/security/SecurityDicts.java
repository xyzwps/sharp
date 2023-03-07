package run.antleg.sharp.config.security;

import jakarta.servlet.http.Cookie;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

public final class SecurityDicts {

    public static final String JWT_COOKIE_NAME = "t";

    /**
     * @see org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler
     * @see RestLoginAuthenticationFilter
     */
    public static final String COOKIE_PATH = "/api";

    public static final Cookie[] COOKIES_TO_CLEAR = new Cookie[]{
            cookieToClear(JWT_COOKIE_NAME, COOKIE_PATH)
    };

    public static final AntPathRequestMatcher LOGIN_REQUEST_MATCHER = new AntPathRequestMatcher("/api/login", "POST");

    public static final AntPathRequestMatcher LOGOUT_REQUEST_MATCHER = new AntPathRequestMatcher("/api/logout", "POST");

    public static final AntPathRequestMatcher ALL_API_REQUEST_MATCHER = new AntPathRequestMatcher("/api/**");

    /**
     * @see org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler
     */
    private static Cookie cookieToClear(String cookieName, String cookiePath) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setPath(cookiePath);
        cookie.setMaxAge(0);
        cookie.setSecure(false); // TODO: 配置此项
        return cookie;
    }
}
