package com.thales.atc.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            // ✅ PROPER CORS CONFIG (FIXED)
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();

                config.setAllowCredentials(false); // ✅ IMPORTANT FIX
                config.addAllowedOriginPattern("*"); // allow all (Codespaces)
                config.addAllowedHeader("*");
                config.addAllowedMethod("*");

                return config;
            }))

            .authorizeHttpRequests(auth -> auth

                // 🌐 PUBLIC READ (Angular dashboard)
                .requestMatchers(HttpMethod.GET, "/api/flights/events/**").permitAll()

                // 👨‍✈️ SUPERVISOR ONLY
                .requestMatchers("/api/flights/events/publish/**").hasRole("SUPERVISOR")

                // 👨‍✈️👨‍✈️ OPERATOR + SUPERVISOR
                .requestMatchers("/api/flights/events/**").hasAnyRole("OPERATOR", "SUPERVISOR")

                .anyRequest().authenticated()
            )

            .httpBasic(Customizer.withDefaults())

            // ❗ prevent redirect → return 401 instead of 302
            .exceptionHandling(e -> e
                .authenticationEntryPoint((req, res, ex) ->
                    res.sendError(401, "Unauthorized"))
            );

        return http.build();
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
        return NoOpPasswordEncoder.getInstance(); // ⚠️ POC only
    }
}