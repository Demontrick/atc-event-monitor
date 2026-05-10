package com.thales.atc.security;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            .authorizeHttpRequests(auth -> auth

                // ✅ ACTUATOR (correct Spring Boot 3 way)
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()

                // 🌐 PUBLIC READ
                .requestMatchers(HttpMethod.GET, "/api/flights/events/**").permitAll()

                // 🚀 PRODUCER TEST ENDPOINT
                .requestMatchers("/api/flights/events/publish/**")
                .hasRole("SUPERVISOR")

                // 🔒 ALL OTHER EVENT OPS
                .requestMatchers("/api/flights/events/**")
                .hasAnyRole("OPERATOR", "SUPERVISOR")

                .anyRequest().authenticated()
            )

            .httpBasic(Customizer.withDefaults())

            .exceptionHandling(e -> e
                .authenticationEntryPoint((req, res, ex) ->
                    res.sendError(401, "Unauthorized"))
            );

        return http.build();
    }

    // 🔥 IMPORTANT FIX: bypass security filter chain entirely for actuator
    @Bean
    public org.springframework.boot.actuate.autoconfigure.security.servlet.WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/actuator/**");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails operator = User.withUsername("operator")
                .password("operator123")
                .roles("OPERATOR")
                .build();

        UserDetails supervisor = User.withUsername("supervisor")
                .password("supervisor123")
                .roles("SUPERVISOR")
                .build();

        return new InMemoryUserDetailsManager(operator, supervisor);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // POC only
    }
}