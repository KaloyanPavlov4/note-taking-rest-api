package com.kaloyan.notetakingapp.service;

import com.kaloyan.notetakingapp.dto.NoteDTO;
import com.kaloyan.notetakingapp.model.Note;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface NoteService {
    Mono<NoteDTO> findById(UUID uuid);

    Flux<NoteDTO> findAll(Pageable pageable);

    Mono<NoteDTO> save(Note note, Authentication authentication);

    Mono<NoteDTO> edit(UUID uuid, Note note, Authentication authentication);

    Mono<Void> deleteById(UUID uuid, Authentication authentication);
}
