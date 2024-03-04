package com.kaloyan.notetakingapp.service;

import com.kaloyan.notetakingapp.dto.UserDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserService {
    Mono<UserDTO> findById(UUID uuid);

    Flux<UserDTO> findAll(Pageable pageable);

    Mono<UserDTO> save(UserDTO userDTO);

    Mono<UserDTO> patchUsername(UUID uuid, String username, Authentication authentication);

    Flux<Void> deleteById(UUID uuid, Authentication authentication);
}
