package com.kaloyan.notetakingapp.controller;

import com.kaloyan.notetakingapp.model.Note;
import com.kaloyan.notetakingapp.service.NoteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    NoteServiceImpl noteService;

    //Gets all notes paginated
    @GetMapping
    public Flux<Note> notes(@PageableDefault Pageable pageable) {
        return noteService.findAll(pageable);
    }

    //Gets note by ID
    @GetMapping("/{id}")
    public Mono<Note> note(@PathVariable("id") UUID noteId) {
        return noteService.findById(noteId);
    }

    //Adds note and associates it with authenticated user.
    @PostMapping
    public Mono<Note> addNote(@RequestBody Note note, Authentication authentication) {
        return noteService.save(note, authentication);
    }

    //Edits note. Checks if it belongs to currently logged-in user. If not returns 401
    @PutMapping("/{id}")
    public Mono<Note> editNote(@PathVariable("id") UUID noteId, @RequestBody Note note, Authentication authentication) {
        return noteService.edit(noteId, note, authentication);
    }

    //Deletes note. Checks if it belongs to the authenticated user. If not returns 401
    @DeleteMapping("/{id}")
    public Mono<Void> deleteNote(@PathVariable("id") UUID noteId, Authentication authentication) {
        return noteService.delete(noteId, authentication);
    }
}
