package com.kaloyan.notetakingapp.controller;

import com.kaloyan.notetakingapp.config.SecurityUtils;
import com.kaloyan.notetakingapp.dto.NoteDTO;
import com.kaloyan.notetakingapp.model.Note;
import com.kaloyan.notetakingapp.service.NoteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    NoteServiceImpl noteService;

    @GetMapping
    public Flux<NoteDTO> notes(@PageableDefault Pageable pageable) {
        return noteService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Mono<NoteDTO> note(@PathVariable("id") UUID noteId) {
        return noteService.findById(noteId);
    }

    @PostMapping
    public Mono<NoteDTO> addNote(@RequestBody Note note) {
        return noteService.save(note, SecurityUtils.currentUsername());
    }

    @PutMapping("/{id}")
    public Mono<NoteDTO> editNote(@PathVariable("id") UUID noteId, @RequestBody Note note) {
        return noteService.edit(noteId, note, SecurityUtils.currentUsername());
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteNote(@PathVariable("id") UUID noteId) {
        return noteService.deleteById(noteId, SecurityUtils.currentUsername());
    }
}
