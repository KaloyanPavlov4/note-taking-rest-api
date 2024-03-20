package com.kaloyan.notetakingapp.service;

import com.kaloyan.notetakingapp.dto.UserDTO;
import com.kaloyan.notetakingapp.model.Role;
import com.kaloyan.notetakingapp.repository.NoteRepository;
import com.kaloyan.notetakingapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class AdminServiceImpl implements AdminService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    NoteRepository noteRepository;

    @Override
    public Flux<Void> deleteUser(UUID userId) {
        return userRepository.findById(userId).flatMapMany(user -> {
            if(user.getRole().equals(Role.ROLE_ADMIN)){
                throw new AccessDeniedException("Admins cannot delete other admins!");
            }
            return Flux.merge(noteRepository.deleteByUserId(userId), userRepository.deleteById(userId));
        });
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
}
