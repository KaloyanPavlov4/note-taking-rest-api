package com.kaloyan.notetakingapp.service;

import com.kaloyan.notetakingapp.exception.DifferentUserException;
import com.kaloyan.notetakingapp.model.Note;
import com.kaloyan.notetakingapp.repository.NoteRepository;
import com.kaloyan.notetakingapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@AllArgsConstructor
@Service
public class NoteServiceImpl implements NoteService{
    @Autowired
    UserRepository userRepository;

    @Autowired
    NoteRepository noteRepository;

    @Override
    public Mono<Note> findById(UUID uuid) {
        return noteRepository.findById(uuid);
    }

    @Override
    public Flux<Note> findAll(Pageable pageable) {
        return noteRepository.findAll().skip(pageable.getPageNumber() * pageable.getPageSize()).take(pageable.getPageSize());;
    }

    @Override
    public Mono<Note> save(Note note) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(currentUsername).flatMap(u -> {
            note.setUser(u);
            return noteRepository.save(note);
        });
    }

    @Override
    public Mono<Note> edit(UUID uuid, Note note) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return noteRepository.findById(uuid).flatMap(n -> {
            if (!currentUsername.equals(n.getUser().getUsername())){
                throw new DifferentUserException("Users can't edit other users' notes!");
            }
            note.setId(uuid);
            return noteRepository.save(note);
        });
    }

    @Override
    public Mono<Note> delete(UUID uuid) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return noteRepository.findById(uuid).flatMap(n -> {
            if (!currentUsername.equals(n.getUser().getUsername())){
                throw new DifferentUserException("Users can't delete other users' notes!");
            };
            noteRepository.deleteById(uuid);
            return Mono.just(n);
        });
    }
}
