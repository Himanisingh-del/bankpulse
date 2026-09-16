package com.bankpulse.bankpulse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .headers(headers ->
                        headers.frameOptions(frame ->
                                frame.sameOrigin()
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // H2 Console
                        .requestMatchers("/h2-console/**").permitAll()

                        // Swagger UI
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()

                        // OpenAPI Documentation
                        .requestMatchers("/v3/api-docs/**").permitAll()

                        // All APIs require authentication
                        .anyRequest().authenticated()
                )

                .httpBasic(httpBasic -> {});

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails user = User.builder()
                .username("bankpulse")
                .password("{noop}BankPulse@123")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
}