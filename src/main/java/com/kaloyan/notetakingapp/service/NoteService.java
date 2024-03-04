package com.kaloyan.notetakingapp.service;

import com.kaloyan.notetakingapp.dto.NoteDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface NoteService {
    Mono<NoteDTO> findById(UUID uuid);

    Flux<NoteDTO> findAll(Pageable pageable);

    Mono<NoteDTO> save(NoteDTO noteDTO, Authentication authentication);

    Mono<NoteDTO> edit(UUID uuid, NoteDTO noteDTO, Authentication authentication);

    Mono<Void> deleteById(UUID uuid, Authentication authentication);
}
