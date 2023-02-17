package run.antleg.sharp.config.security;

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
import run.antleg.sharp.util.JSON;

import java.util.Map;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @SuppressWarnings("Convert2MethodRef")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/home", "/api/login").permitAll()
                        .requestMatchers("/hello").authenticated()
                        .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf.disable())
                .formLogin((form) -> form.disable())
                .addFilterAt(new RestLoginAuthenticationFilter(JSON.OBJECT_MAPPER, authenticationConfiguration.getAuthenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .logout((logout) -> logout.permitAll());

        // TODO: log filter chain

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);

        var passwordEncoder = new DelegatingPasswordEncoder("noop", Map.of(
                "noop", NoOpPasswordEncoder.getInstance() // TODO: 换个正经的密码，要等有注册功能后才行
        ));
        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }
}
