package com.kaloyan.notetakingapp.service;

import com.kaloyan.notetakingapp.config.SecurityUtils;
import com.kaloyan.notetakingapp.dto.NoteDTO;
import com.kaloyan.notetakingapp.exception.DifferentUserException;
import com.kaloyan.notetakingapp.model.Note;
import com.kaloyan.notetakingapp.repository.NoteRepository;
import com.kaloyan.notetakingapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@AllArgsConstructor
@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    NoteRepository noteRepository;

    //Reactive repositories do not support joins so this method is used to populate the user field of the note objects
    private Mono<Note> noteWithUser(UUID noteId) {
        return noteRepository.findById(noteId).flatMap(note -> userRepository.findById(note.getUserId()).map(user -> {
            note.setUser(user);
            return note;
        }));
    }

    @Override
    public Mono<NoteDTO> findById(UUID uuid) {
        return noteWithUser(uuid).map(NoteDTO::new);
    }

    //Reactive repositories do not support pagination with Pageable so it is done by skipping pageNumber*pageSize entries and then taking pageSize entries
    @Override
    public Flux<NoteDTO> findAll(Pageable pageable) {
        return noteRepository.findAll().flatMap(note -> noteWithUser(note.getId())).map(NoteDTO::new)
                .skip(pageable.getPageNumber() * pageable.getPageSize()).take(pageable.getPageSize());
    }

    @Override
    public Mono<NoteDTO> save(NoteDTO noteDto) {
        Note note = new Note(noteDto);
        return SecurityUtils.authenticatedUsername().flatMap(username -> userRepository.findByUsername(username).flatMap(u -> {
            note.setUserId(u.getId());
            note.setUser(u);
            return noteRepository.save(note);
        }).map(NoteDTO::new));
    }

    @Override
    public Mono<NoteDTO> edit(UUID uuid, NoteDTO noteDTO) {
        Note note = new Note(noteDTO);
        return SecurityUtils.authenticatedUsername().flatMap(username -> userRepository.findByUsername(username).flatMap(user -> noteRepository.findById(uuid).flatMap(n -> {
            if (!username.equals(user.getUsername())) {
                throw new DifferentUserException("Users can't edit other users' notes!");
            }
            note.setId(uuid);
            note.setUserId(user.getId());
            note.setUser(user);
            return noteRepository.save(note);
        })).map(NoteDTO::new));
    }

    @Override
    public Mono<Void> deleteById(UUID uuid) {
        return SecurityUtils.authenticatedUsername().flatMap(username -> noteWithUser(uuid).flatMap(note -> {
            if(!username.equals(note.getUser().getUsername())){
                throw new DifferentUserException("Users can't delete other users' notes!");
            }
            return noteRepository.deleteById(uuid);
        }));
    }
}
