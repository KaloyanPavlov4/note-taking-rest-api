package com.kaloyan.notetakingapp.service;

import com.kaloyan.notetakingapp.config.SecurityUtils;
import com.kaloyan.notetakingapp.dto.UserDTO;
import com.kaloyan.notetakingapp.exception.DifferentUserException;
import com.kaloyan.notetakingapp.model.Role;
import com.kaloyan.notetakingapp.model.User;
import com.kaloyan.notetakingapp.repository.NoteRepository;
import com.kaloyan.notetakingapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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

    private Mono<UserDTO> userWithNotes(User user) {
        return noteRepository.findByUserId(user.getId()).collectList().flatMap(notes -> {
            user.setNotes(notes);
            return Mono.just(new UserDTO(user));
        });
    }

    @Override
    public Mono<UserDTO> findById(UUID uuid) {
        return userRepository.findById(uuid).flatMap(this::userWithNotes);
    }

    @Override
    public Flux<UserDTO> findAll(Pageable pageable) {
        return userRepository.findAll().flatMap(this::userWithNotes)
                .skip(pageable.getPageNumber() * pageable.getPageSize()).take(pageable.getPageSize());
    }

    @Override
    public Mono<UserDTO> save(UserDTO userDTO) {
        User user = new User(userDTO);
        user.setRole(Role.ROLE_USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user).map(UserDTO::new);
    }

    @Override
    public Mono<UserDTO> patchUsername(UUID uuid, String username) {
        return SecurityUtils.authenticatedUsername().flatMap(authUser -> userRepository.findById(uuid).flatMap(u -> {
            if (!u.getUsername().equals(authUser)) {
                throw new DifferentUserException("Users are forbidden from changing other Users!");
            }
            u.setUsername(username);
            return userRepository.save(u).flatMap(this::userWithNotes);
        }));
    }

    @Override
    public Flux<Void> deleteById(UUID uuid) {
        return SecurityUtils.authenticatedUsername().flatMapMany(username -> userRepository.findById(uuid).flatMapMany(u -> {
                    if (!u.getUsername().equals(username)) {
                        throw new DifferentUserException("Users are forbidden from deleting other Users!");
                    }
                    return Flux.merge(noteRepository.deleteByUserId(uuid), userRepository.deleteById(uuid));
                }
        ));
    }
}
