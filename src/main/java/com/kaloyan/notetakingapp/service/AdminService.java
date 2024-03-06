package com.kaloyan.notetakingapp.service;

import com.kaloyan.notetakingapp.dto.UserDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AdminService {

    Flux<Void> deleteUser(UUID userId);

    Mono<Void> deleteNote(UUID noteId);

    Mono<UserDTO> makeUserAdmin(UUID userId);
}