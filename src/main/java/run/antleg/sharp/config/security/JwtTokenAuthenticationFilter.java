package run.antleg.sharp.config.security;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static run.antleg.sharp.config.security.SecurityDicts.*;

@Slf4j
public class JwtTokenAuthenticationFilter implements Filter {


    private final AuthenticationManager authenticationManager;
    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();
    protected ApplicationEventPublisher eventPublisher; // TODO: 搞清楚作用


    protected JwtTokenAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    protected boolean requiresAuthentication(HttpServletRequest request) {
        return ALL_API_REQUEST_MATCHER.matches(request) && !LOGIN_REQUEST_MATCHER.matches(request);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (!requiresAuthentication(request)) {
            chain.doFilter(request, response);
            return;
        }

        attemptAuthentication(request)
                .filter(Authentication::isAuthenticated)
                .ifPresent(authentication -> this.successfulAuthentication(request, response, authentication));

        chain.doFilter(request, response);
    }

    /**
     * FIXME: 这里参考 {@link AbstractAuthenticationProcessingFilter}，但是没搞清楚 remember me 到底是什么，先把它删掉，日后再补回来。
     */
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) {
        SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authResult);
        this.securityContextHolderStrategy.setContext(context);
        this.securityContextRepository.saveContext(context, request, response);
        log.debug("Set SecurityContextHolder to {}", authResult);
//        this.rememberMeServices.loginSuccess(request, response, authResult);
        if (this.eventPublisher != null) {
            this.eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(authResult, this.getClass()));
        }
    }

    private Optional<Authentication> attemptAuthentication(HttpServletRequest request) throws AuthenticationException {
        return getJwtCookie(request)
                .map(Cookie::getValue)
                .map(s -> this.authenticationManager.authenticate(JwtAuthenticationToken.unauthenticated(s)));

    }

    private Optional<Cookie> getJwtCookie(HttpServletRequest request) {
        return Optional.ofNullable(request.getCookies()).map(Arrays::asList).orElse(List.of()).stream()
                .filter(cookie -> JWT_COOKIE_NAME.equals(cookie.getName()))
                .findFirst();
    }

    @SuppressWarnings("unused")
    public void setSecurityContextHolderStrategy(SecurityContextHolderStrategy securityContextHolderStrategy) {
        Assert.notNull(securityContextHolderStrategy, "securityContextHolderStrategy cannot be null");
        this.securityContextHolderStrategy = securityContextHolderStrategy;
    }

    @SuppressWarnings("unused")
    public void setSecurityContextRepository(SecurityContextRepository securityContextRepository) {
        Assert.notNull(securityContextRepository, "securityContextRepository cannot be null");
        this.securityContextRepository = securityContextRepository;
    }

    @SuppressWarnings("unused")
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
}
