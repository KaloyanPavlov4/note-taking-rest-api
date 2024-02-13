package com.kaloyan.notetakingapp.controller;


import com.kaloyan.notetakingapp.model.Note;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/notes")
public class NoteController {

    //Gets all notes paginated
    @GetMapping
    public Flux<Note> notes(@RequestParam Pageable pageable) {
        return Flux.empty();
    }

    //Gets note by ID
    @GetMapping("/{id}")
    public Mono<Note> note(@PathVariable("id") UUID noteId) {
        return Mono.empty();
    }

    //Adds note and associates it with currently logged-in user.
    @PostMapping
    public Mono<Note> addNote(@RequestBody Note note, Principal principal) {
        return Mono.empty();
    }

    //Edits note. Checks if it belongs to currently logged-in user. If not returns 403 forbidden
    @PutMapping("/{id}")
    public Mono<Note> editNote(@PathVariable("id") UUID noteId, @RequestBody Note note, Principal principal) {
        return Mono.empty();
    }

    //Deletes note. Checks if it belongs to currently logged-in user. If not returns 403 forbidden
    @DeleteMapping
    public Mono<Note> deleteNote(@PathVariable("id") UUID noteId, Principal principal) {
        return Mono.empty();
    }
}
