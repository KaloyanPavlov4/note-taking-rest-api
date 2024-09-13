package com.kaloyan.notetakingapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                .pathMatchers(HttpMethod.GET, "/**").permitAll()
                .pathMatchers(HttpMethod.POST, "/users").access(this::isNotAuthenticated)
                .pathMatchers("/admin").hasRole("ROLE_ADMIN").anyExchange().authenticated()).httpBasic(Customizer.withDefaults());
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        return http.build();
    }

    private Mono<AuthorizationDecision> isNotAuthenticated(Mono<Authentication> authenticationMono, AuthorizationContext context) {
        return authenticationMono
                .map(authentication -> new AuthorizationDecision(!authentication.isAuthenticated()))
                .defaultIfEmpty(new AuthorizationDecision(true));
    }
}