package com.kaloyan.notetakingapp.service;

import com.kaloyan.notetakingapp.model.Note;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface NoteService {
    Mono<Note> findById(UUID uuid);

    Flux<Note> findAll(Pageable pageable);

    Mono<Note> save(Note note, Authentication authentication);

    Mono<Note> edit(UUID uuid, Note note, Authentication authentication);

    Mono<Void> delete(UUID uuid, Authentication authentication);
}