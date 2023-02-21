package run.antleg.sharp.config.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import run.antleg.sharp.modules.user.security.MyUserDetailsService;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtService jwtService;

    private final MyUserDetailsService userDetailsService;

    public JwtAuthenticationProvider(JwtService jwtService, MyUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var unauth = (JwtAuthenticationToken) authentication;
        var jwt = unauth.getJwt();

        var result = jwtService.verify(jwt);
        if (result.isRight()) {
            var userId = result.get();
            var $userDetails = userDetailsService.loadUserByUserId(userId);
            if ($userDetails.isEmpty()) return null;
            var userDetails = $userDetails.get();
            return JwtAuthenticationToken.authenticated(userDetails, userDetails.getAuthorities());
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
