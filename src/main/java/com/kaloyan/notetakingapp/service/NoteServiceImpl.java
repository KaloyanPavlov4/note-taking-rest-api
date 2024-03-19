package com.kaloyan.notetakingapp.service;

import com.kaloyan.notetakingapp.dto.NoteDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class NoteServiceImpl implements NoteService {
    @Override
    public Mono<NoteDTO> findById(UUID uuid) {
        return null;
    }

    @Override
    public Flux<NoteDTO> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Mono<NoteDTO> save(NoteDTO noteDTO) {
        return null;
    }

    @Override
    public Mono<NoteDTO> edit(UUID uuid, NoteDTO noteDTO) {
        return null;
    }

    @Override
    public Mono<Void> deleteById(UUID uuid) {
        return null;
    }
}
