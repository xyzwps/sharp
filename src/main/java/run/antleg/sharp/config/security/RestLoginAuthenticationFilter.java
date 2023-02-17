package run.antleg.sharp.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;

/**
 * @see org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
 */
public class RestLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher("/api/login", "POST");

    private final ObjectMapper objectMapper;

    public RestLoginAuthenticationFilter(ObjectMapper objectMapper, AuthenticationManager authenticationManager) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        super.setAuthenticationManager(authenticationManager);
        this.objectMapper = Objects.requireNonNull(objectMapper);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        this.onlyAcceptPost(request);
        this.onlyAcceptJson(request);

        var payload = this.obtainPayload(request);

        var authentication = UsernamePasswordAuthenticationToken.unauthenticated(payload.username, payload.password);
        var details = this.authenticationDetailsSource.buildDetails(request);
        authentication.setDetails(details);
        return this.getAuthenticationManager().authenticate(authentication);
    }

    private LoginPayload obtainPayload(HttpServletRequest request) {
        try {
            return objectMapper.readValue(request.getInputStream(), LoginPayload.class);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    // TODO: 处理成功后的数据返回问题

    public static class LoginPayload {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    private void onlyAcceptPost(HttpServletRequest request) {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
    }

    private void onlyAcceptJson(HttpServletRequest request) {
        var contentType = request.getContentType();
        if (contentType == null || !contentType.toLowerCase().startsWith(MediaType.APPLICATION_JSON_VALUE)) {
            throw new HttpClientErrorException(HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
