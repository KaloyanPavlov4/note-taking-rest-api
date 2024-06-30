package com.kaloyan.notetakingapp.service;

import com.kaloyan.notetakingapp.dto.UserDTO;
import com.kaloyan.notetakingapp.model.Role;
import com.kaloyan.notetakingapp.repository.NoteRepository;
import com.kaloyan.notetakingapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    NoteRepository noteRepository;

    @Override
    public Mono<Void> deleteUser(UUID userId) {
        return userRepository.deleteById(userId).then(noteRepository.deleteByUserId(userId));
    }

    @Override
    public Mono<Void> deleteNote(UUID noteId) {
        return noteRepository.deleteById(noteId);
    }

    @Override
    public Mono<UserDTO> makeUserAdmin(UUID userId) {
        return userRepository.findById(userId).flatMap(user -> {
            user.setRole(Role.ROLE_ADMIN);
            return userRepository.save(user).map(UserDTO::new);
        });
    }

    @Override
    public Mono<Boolean> isNotAdmin(UUID userId) {
        return userRepository.findById(userId).map(user -> !user.getRole().equals(Role.ROLE_ADMIN));
    }
}
