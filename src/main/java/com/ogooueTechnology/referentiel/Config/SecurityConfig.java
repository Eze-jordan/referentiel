package com.ogooueTechnology.referentiel.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(AbstractHttpConfigurer::disable) // ← Active CORS ici
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/api/utilisateurs/**",
                                "/api/utilisateurs/create",
                                "/api/utilisateurs/activation",
                                "/api/utilisateurs/connexion",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api/v1/referentiel",
                                "/api/v1/referentiel/**"
                                ).permitAll()
                        .anyRequest().authenticated()
                )

                .build();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

