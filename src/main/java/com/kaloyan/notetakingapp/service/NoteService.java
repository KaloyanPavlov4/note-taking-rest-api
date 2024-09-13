package com.kaloyan.notetakingapp.service;

import com.kaloyan.notetakingapp.dto.NoteDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service interface for managing notes.
 * Provides operations to find, save, edit, and delete notes.
 */
public interface NoteService {

    /**
     * Retrieves a note by its UUID.
     *
     * @param uuid the UUID of the note to be retrieved
     * @return a {@link Mono} containing the {@link NoteDTO} with the specified UUID, or empty if not found
     */
    Mono<NoteDTO> findById(UUID uuid);

    /**
     * Retrieves all notes with pagination support.
     *
     * @param pageable the {@link Pageable} object specifying pagination information
     * @return a {@link Flux} containing {@link NoteDTO}s for the requested page
     */
    Flux<NoteDTO> findAll(Pageable pageable);

    /**
     * Saves a new note or updates an existing note.
     *
     * @param noteDTO the {@link NoteDTO} to be saved or updated
     * @return a {@link Mono} containing the saved {@link NoteDTO}
     */
    Mono<NoteDTO> save(NoteDTO noteDTO);

    /**
     * Edits an existing note identified by its UUID.
     *
     * @param uuid the UUID of the note to be edited
     * @param noteDTO the {@link NoteDTO} containing updated information for the note
     * @return a {@link Mono} containing the updated {@link NoteDTO}
     */
    Mono<NoteDTO> edit(UUID uuid, NoteDTO noteDTO);

    /**
     * Deletes a note by its UUID.
     *
     * @param uuid the UUID of the note to be deleted
     * @return a {@link Mono} that completes when the note is deleted
     */
    Mono<Void> deleteById(UUID uuid);
}
