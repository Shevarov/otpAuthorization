package by.otp.access.config;

import by.otp.access.security.entrypoint.JwtAccessDeniedHandler;
import by.otp.access.security.entrypoint.JwtAuthenticationEntryPoint;
import by.otp.access.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String[] SWAGGER_URLS = {
            "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**"
    };

    private static final String PROMETHEUS_URL = "/actuator/prometheus";

    private static final String[] AUTH_URLS = {
            "/auth/**"
    };

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
                                                     JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                                                     JwtAccessDeniedHandler jwtAccessDeniedHandler) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(conf -> conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(conf -> conf
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(SWAGGER_URLS).permitAll()
                        .requestMatchers(AUTH_URLS).permitAll()
                        .requestMatchers(PROMETHEUS_URL).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
