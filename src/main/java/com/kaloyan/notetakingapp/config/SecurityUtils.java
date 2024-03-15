package com.kaloyan.notetakingapp.config;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import reactor.core.publisher.Mono;

public class SecurityUtils {

    public static Mono<String> authenticatedUsername(){
        return ReactiveSecurityContextHolder.getContext().map(securityContext -> securityContext.getAuthentication().getName());
    }
}