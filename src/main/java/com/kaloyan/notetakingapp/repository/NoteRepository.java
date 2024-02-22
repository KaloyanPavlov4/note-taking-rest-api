package com.kaloyan.notetakingapp.repository;

import com.kaloyan.notetakingapp.model.Note;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface NoteRepository extends ReactiveCrudRepository<Note, UUID> {
    @Query("select * from notes where user_Id=$1")
    Flux<Note> findAllByUser(UUID userId);

    @Query("delete from notes where user_Id=$1")
    Mono<Void> deleteAllNotesByUser(UUID userId);
}