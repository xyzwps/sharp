package run.antleg.sharp.config.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String jwt;

    private final UserDetails userDetails;

    public JwtAuthenticationToken(String jwt, UserDetails userDetails, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.jwt = jwt;
        this.userDetails = userDetails;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userDetails;
    }

    public String getJwt() {
        return jwt;
    }


    public static JwtAuthenticationToken unauthenticated(String jwt) {
        var token = new JwtAuthenticationToken(jwt, null, null);
        token.setAuthenticated(false);
        return token;
    }

    public static JwtAuthenticationToken authenticated(UserDetails userDetails, Collection<? extends GrantedAuthority> authorities) {
        var token = new JwtAuthenticationToken(null, userDetails, authorities);
        token.setAuthenticated(true);
        return token;
    }
}
