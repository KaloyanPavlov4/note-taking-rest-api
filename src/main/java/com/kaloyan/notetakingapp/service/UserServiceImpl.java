package com.kaloyan.notetakingapp.service;

import com.kaloyan.notetakingapp.exception.DifferentUserException;
import com.kaloyan.notetakingapp.model.Role;
import com.kaloyan.notetakingapp.model.User;
import com.kaloyan.notetakingapp.repository.NoteRepository;
import com.kaloyan.notetakingapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    NoteRepository noteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Mono<User> userWithNotes(UUID userId) {
        return userRepository.findById(userId).flatMap(user -> noteRepository.findAllByUser(userId).collectList().map(notes -> {
            user.setNotes(notes);
            return user;
        }));
    }

    @Override
    public Mono<User> findById(UUID uuid) {
        return userWithNotes(uuid);
    }

    @Override
    public Flux<User> findAll(Pageable pageable) {
        return userRepository.findAll().flatMap(user -> userWithNotes(user.getId()))
                .skip(pageable.getPageNumber() * pageable.getPageSize()).take(pageable.getPageSize());
    }

    @Override
    public Mono<User> save(User user) {
        user.setRole(Role.ROLE_USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    //Maybe not best way to return deleted user
    @Override
    public Flux<Void> deleteById(UUID uuid, Authentication authentication) {
        String currentUsername = authentication.getName();
        return userRepository.findById(uuid).flatMapMany(u -> {
                    if (!u.getUsername().equals(currentUsername)) {
                        throw new DifferentUserException("Users are forbidden from deleting other Users!");
                    }
                    return Flux.merge(noteRepository.deleteAllNotesByUser(uuid), userRepository.deleteById(uuid));
                }
        );
    }

    @Override
    public Mono<User> patchUsername(UUID uuid, String username, Authentication authentication) {
        String currentUsername = authentication.getName();
        return userRepository.findById(uuid).flatMap(u -> {
            if (!u.getUsername().equals(currentUsername)) {
                throw new DifferentUserException("Users are forbidden from changing other Users!");
            }
            u.setUsername(username);
            authentication.setAuthenticated(false);
            return userRepository.save(u).flatMap(user -> userWithNotes(user.getId()));
        });
    }
}
