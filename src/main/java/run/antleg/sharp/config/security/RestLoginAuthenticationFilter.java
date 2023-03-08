package run.antleg.sharp.config.security;

import jakarta.servlet.http.Cookie;
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
import org.springframework.validation.BindException;
import org.springframework.validation.DirectFieldBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.client.HttpClientErrorException;
import run.antleg.sharp.modules.errors.AppException;
import run.antleg.sharp.modules.errors.Errors;
import run.antleg.sharp.modules.user.model.UserService;
import run.antleg.sharp.modules.user.security.MyUserDetails;
import run.antleg.sharp.modules.errors.HandleExceptions;
import run.antleg.sharp.util.JSON;
import run.antleg.sharp.util.Servlets;

import java.io.IOException;

/**
 * @see org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
 */
public class RestLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final Validator validator;
    private final JwtService jwtService;
    private final UserService userService;

    public RestLoginAuthenticationFilter(UserService userService,
                                         Validator validator,
                                         JwtService jwtService,
                                         AuthenticationManager authenticationManager) {
        super(SecurityDicts.LOGIN_REQUEST_MATCHER);
        super.setAuthenticationManager(authenticationManager);
        super.setAuthenticationSuccessHandler(this::onAuthenticationSuccess);
        super.setAuthenticationFailureHandler(this::onAuthenticationFailure);
        this.validator = validator;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        this.onlyAcceptPost(request);
        this.onlyAcceptJson(request);

        var payload = this.obtainPayload(request);

        var unauth = UsernamePasswordAuthenticationToken.unauthenticated(payload.getUsername(), payload.getPassword());
        var details = this.authenticationDetailsSource.buildDetails(request);
        unauth.setDetails(details);
        return this.getAuthenticationManager().authenticate(unauth);
    }

    private LoginPayload obtainPayload(HttpServletRequest request) throws IOException {
        // TODO: 做更细致的检查
        var payload = JSON.parse(request.getInputStream(), LoginPayload.class);
        var br = new DirectFieldBindingResult(payload, payload.getClass().getCanonicalName());
        validator.validate(payload, br);
        if (br.hasErrors()) {
            throw new AuthenticationServiceException("登录参数检查错误", new BindException(br));
        }
        return payload;
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

    private void onAuthenticationSuccess(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Authentication authentication) {
        var userDetails = (MyUserDetails) authentication.getPrincipal();
        var user = userService.findUserById(userDetails.getUserId())
                .orElseThrow(() -> new AppException(Errors.IMPOSSIBLE));
        var cookie = new Cookie(SecurityDicts.JWT_COOKIE_NAME, jwtService.makeJwt(userDetails)); // TODO: 更多设置
        cookie.setPath(SecurityDicts.COOKIE_PATH);
        response.addCookie(cookie);
        Servlets.sendJson(response, HttpStatus.OK, user);
    }

    private void onAuthenticationFailure(HttpServletRequest request,
                                         HttpServletResponse response,
                                         AuthenticationException exception) {
        var cause = exception.getCause();
        if (cause == null) {
            HandleExceptions.write(exception, response);
            return;
        } else if (cause instanceof BindException ex) {
            HandleExceptions.write(ex, response);
            return;
        }

        HandleExceptions.write(exception, response);
    }
}
