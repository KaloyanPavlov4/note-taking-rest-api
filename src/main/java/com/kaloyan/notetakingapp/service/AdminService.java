package com.kaloyan.notetakingapp.service;

import com.kaloyan.notetakingapp.dto.UserDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AdminService {

    Mono<Void> deleteUser(UUID userId);

    Mono<Void> deleteNote(UUID noteId);

    Mono<UserDTO> makeUserAdmin(UUID userId);

    /**
     * SPeL throws exception when trying to get the opposite result of a Mono<Boolean>
     * which is why the method in AdminService is "isNotAdmin" instead of "isAdmin"
     * */
    Mono<Boolean> isNotAdmin(UUID userId);
}
