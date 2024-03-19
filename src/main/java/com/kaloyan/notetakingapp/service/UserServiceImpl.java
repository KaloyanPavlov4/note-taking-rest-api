package com.kaloyan.notetakingapp.service;

import com.kaloyan.notetakingapp.dto.UserDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{
    @Override
    public Mono<UserDTO> findById(UUID uuid) {
        return null;
    }

    @Override
    public Flux<UserDTO> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Mono<UserDTO> save(UserDTO userDTO) {
        return null;
    }

    @Override
    public Mono<UserDTO> patchUsername(UUID uuid, String username) {
        return null;
    }

    @Override
    public Flux<Void> deleteById(UUID uuid) {
        return null;
    }
}
