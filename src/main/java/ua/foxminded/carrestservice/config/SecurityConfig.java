package ua.foxminded.carrestservice.config;

import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for Spring Security in the car rest service system.
 * Annotated with {@code @Configuration} to mark it as a Spring configuration class and
 * {@code @EnableWebSecurity} to enable Spring Security. Configures HTTP security, including
 * authorization rules, OAuth2 JWT-based authentication, stateless session management, and
 * CSRF protection disablement.
 *
 * @author Serhii Bohdan
 * @see org.springframework.context.annotation.Configuration
 * @see org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
 * @see org.springframework.security.web.SecurityFilterChain
 * @see org.springframework.security.config.annotation.web.builders.HttpSecurity
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the security filter chain for HTTP requests.
     * Permits all GET requests, requires authentication for all other requests, uses OAuth2 JWT
     * authentication, enforces stateless session management, and disables CSRF protection.
     *
     * @param http the {@link HttpSecurity} object to configure security settings
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET).permitAll()
                .anyRequest().authenticated())
            .oauth2ResourceServer(configurer -> configurer.jwt(withDefaults()))
            .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(CsrfConfigurer::disable)
            .build();
    }

}
