package com.kaloyan.notetakingapp.service;

import com.kaloyan.notetakingapp.model.User;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.UUID;

public interface UserService {
    Mono<User> findById(UUID uuid);

    Flux<User> findAll(Pageable pageable);

    Mono<User> save(User user);

    Mono<User> deleteById(UUID uuid);

    Mono<User> patchUsername(UUID uuid, String username);
}
