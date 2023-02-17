package run.antleg.sharp.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.validation.Validator;
import run.antleg.sharp.modules.user.UserHandler;
import run.antleg.sharp.util.JSON;

import java.util.Map;

@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig {

    @SuppressWarnings("Convert2MethodRef")
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurityFilterChainBuilder,
            AuthenticationConfiguration authConfig,
            UserHandler userHandler,
            Validator validator
    ) throws Exception {

        httpSecurityFilterChainBuilder
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/home", "/api/login").permitAll()
                        .requestMatchers("/hello").authenticated()
                        .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf.disable())
                .formLogin((form) -> form.disable())
                .addFilterAt(new RestLoginAuthenticationFilter(userHandler, validator, authConfig.getAuthenticationManager()),
                        UsernamePasswordAuthenticationFilter.class)
                .logout((logout) -> logout.permitAll());

        var chain = httpSecurityFilterChainBuilder.build();
        log.info("Chained Security filters are following:");
        chain.getFilters().forEach(filter -> log.info("  ▶ {}", filter.getClass().getSimpleName()));
        return chain;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
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
