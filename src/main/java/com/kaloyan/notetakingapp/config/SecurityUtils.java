package com.kaloyan.notetakingapp.config;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import reactor.core.publisher.Mono;

public class SecurityUtils {

    public static Mono<String> currentUsername(){
        return ReactiveSecurityContextHolder.getContext().map(securityContext -> securityContext.getAuthentication().getName());
    }
}
