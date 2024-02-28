package com.kaloyan.notetakingapp.service;

import com.kaloyan.notetakingapp.dto.NoteDTO;
import com.kaloyan.notetakingapp.exception.DifferentUserException;
import com.kaloyan.notetakingapp.model.Note;
import com.kaloyan.notetakingapp.repository.NoteRepository;
import com.kaloyan.notetakingapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
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

    @Override
    public Flux<NoteDTO> findAll(Pageable pageable) {
        return noteRepository.findAll().flatMap(note -> noteWithUser(note.getId())).map(NoteDTO::new)
                .skip(pageable.getPageNumber() * pageable.getPageSize()).take(pageable.getPageSize());
    }

    @Override
    public Mono<NoteDTO> save(Note note, Authentication authentication) {
        return userRepository.findByUsername(authentication.getName()).flatMap(u -> {
            note.setUserId(u.getId());
            note.setUser(u);
            return noteRepository.save(note);
        }).map(NoteDTO::new);
    }

    @Override
    public Mono<NoteDTO> edit(UUID uuid, Note note, Authentication authentication) {
        String currentUsername = authentication.getName();
        return userRepository.findByUsername(currentUsername).flatMap(user -> noteRepository.findById(uuid).flatMap(n -> {
            if (!currentUsername.equals(user.getUsername())) {
                throw new DifferentUserException("Users can't edit other users' notes!");
            }
            note.setId(uuid);
            note.setUserId(user.getId());
            note.setUser(user);
            return noteRepository.save(note);
        })).map(NoteDTO::new);
    }

    @Override
    public Mono<Void> deleteById(UUID uuid, Authentication authentication) {
        String currentUsername = authentication.getName();
        return noteWithUser(uuid).flatMap(note -> {
            if(!currentUsername.equals(note.getUser().getUsername())){
                throw new DifferentUserException("Users can't delete other users' notes!");
            }
            return noteRepository.deleteById(uuid);
        });

    }
}
