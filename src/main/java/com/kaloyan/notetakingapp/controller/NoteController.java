package com.kaloyan.notetakingapp.controller;

import com.kaloyan.notetakingapp.dto.NoteDTO;
import com.kaloyan.notetakingapp.service.NoteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PutMapping;
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
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<NoteDTO> addNote(@RequestBody NoteDTO noteDTO, Authentication authentication) {
        return noteService.save(noteDTO, authentication);
    }

    @PutMapping("/{id}")
    public Mono<NoteDTO> editNote(@PathVariable("id") UUID noteId, @RequestBody NoteDTO noteDTO, Authentication authentication) {
        return noteService.edit(noteId, noteDTO, authentication);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteNote(@PathVariable("id") UUID noteId, Authentication authentication) {
        return noteService.deleteById(noteId, authentication);
    }
}
