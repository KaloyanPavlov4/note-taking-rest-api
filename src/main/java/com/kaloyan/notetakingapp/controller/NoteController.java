package com.kaloyan.notetakingapp.controller;

import com.kaloyan.notetakingapp.dto.NoteDTO;
import com.kaloyan.notetakingapp.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    NoteService noteService;

    @GetMapping
    public Flux<NoteDTO> notes(@PageableDefault Pageable pageable) {
        return noteService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Mono<NoteDTO> note(@PathVariable("id") UUID noteId) {
        return noteService.findById(noteId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<NoteDTO> addNote(@Valid @RequestBody NoteDTO note) {
        return noteService.save(note);
    }

    @PutMapping("/{id}")
    public Mono<NoteDTO> editNote(@PathVariable("id") UUID noteId, @Valid @RequestBody NoteDTO noteDTO) {
        return noteService.edit(noteId, noteDTO);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteNote(@PathVariable("id") UUID noteId) {
        return noteService.deleteById(noteId);
    }
}