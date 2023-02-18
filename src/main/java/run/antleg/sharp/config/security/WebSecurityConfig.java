package run.antleg.sharp.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.validation.Validator;
import run.antleg.sharp.modules.user.UserHandler;
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
            JwtAuthenticationProvider jwtAuthenticationProvider
    ) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(jwtAuthenticationProvider)
                .authenticationProvider(daoAuthenticationProvider(myUserDetailsService))
                .build();
    }

    @SuppressWarnings("Convert2MethodRef")
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurityFilterChainBuilder,
            UserHandler userHandler,
            Validator validator,
            JwtService jwtService,
            AuthenticationManager authenticationManager
    ) throws Exception {
        httpSecurityFilterChainBuilder
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/home", "/api/login").permitAll()
                        .requestMatchers("/hello").authenticated()
                        .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf.disable())
                .formLogin((form) -> form.disable())
                .authenticationManager(authenticationManager)
                .addFilterAt(new RestLoginAuthenticationFilter(userHandler, validator, jwtService, authenticationManager),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtTokenAuthenticationFilter(authenticationManager),
                        RestLoginAuthenticationFilter.class)
                .logout((logout) -> logout.permitAll());

        var chain = httpSecurityFilterChainBuilder.build();
        log.info("Chained Security filters are following:");
        chain.getFilters().forEach(filter -> log.info("  ▶ {}", filter.getClass().getSimpleName()));
        return chain;
    }

    private AuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService) {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);

        var passwordEncoder = new DelegatingPasswordEncoder("noop", Map.of(
                "noop", NoOpPasswordEncoder.getInstance() // TODO: 换个正经的密码，要等有注册功能后才行
        ));
        provider.setPasswordEncoder(passwordEncoder);
        provider.setHideUserNotFoundExceptions(false);

        return provider;
    }

}
