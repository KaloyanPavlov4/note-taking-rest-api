package com.kaloyan.notetakingapp.service;

import com.kaloyan.notetakingapp.dto.UserDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class AdminServiceImpl implements AdminService {
    @Override
    public Flux<Void> deleteUser(UUID userId) {
        return null;
    }

    @Override
    public Mono<Void> deleteNote(UUID noteId) {
        return null;
    }

    @Override
    public Mono<UserDTO> makeUserAdmin(UUID userId) {
        return null;
    }
}