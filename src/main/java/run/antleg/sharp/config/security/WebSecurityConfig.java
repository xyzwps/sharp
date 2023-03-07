package run.antleg.sharp.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.validation.Validator;
import run.antleg.sharp.modules.user.model.UserService;
import run.antleg.sharp.modules.user.security.MyUserDetailsService;

import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@Slf4j
public class WebSecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http,
            MyUserDetailsService myUserDetailsService,
            JwtAuthenticationProvider jwtAuthenticationProvider,
            PasswordEncoder passwordEncoder
    ) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(jwtAuthenticationProvider)
                .authenticationProvider(daoAuthenticationProvider(myUserDetailsService, passwordEncoder))
                .build();
    }

    @SuppressWarnings("Convert2MethodRef")
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurityFilterChainBuilder,
            UserService userService,
            Validator validator,
            JwtService jwtService,
            AuthenticationManager authenticationManager
    ) throws Exception {
        httpSecurityFilterChainBuilder
                .authorizeHttpRequests((requests) -> requests.anyRequest().permitAll())
                .csrf(csrf -> csrf.disable())
                .formLogin((form) -> form.disable())
                .authenticationManager(authenticationManager)
                .anonymous().disable()
                .addFilterAt(new RestLoginAuthenticationFilter(userService, validator, jwtService, authenticationManager),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtTokenAuthenticationFilter(authenticationManager),
                        RestLoginAuthenticationFilter.class)
                .logout(logout -> {
                    logout.logoutRequestMatcher(SecurityDicts.LOGOUT_REQUEST_MATCHER);
                    logout.addLogoutHandler(new CookieClearingLogoutHandler(SecurityDicts.COOKIES_TO_CLEAR));
                    logout.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT));
                });

        var chain = httpSecurityFilterChainBuilder.build();
        log.info("Chained Security filters are following:");
        chain.getFilters().forEach(filter -> log.info("  ▶ {}", filter.getClass().getSimpleName()));
        return chain;
    }

    private AuthenticationProvider daoAuthenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        provider.setHideUserNotFoundExceptions(false);

        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new DelegatingPasswordEncoder("pbkdf2", Map.of(
                "pbkdf2", Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8()
        ));
    }

}
