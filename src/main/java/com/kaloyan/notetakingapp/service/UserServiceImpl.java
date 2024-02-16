package com.kaloyan.notetakingapp.service;

import com.kaloyan.notetakingapp.exception.DifferentUserException;
import com.kaloyan.notetakingapp.model.User;
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
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public Mono<User> findById(UUID uuid) {
        return userRepository.findById(uuid);
    }

    @Override
    public Flux<User> findAll(Pageable pageable) {
        return userRepository.findAll().skip(pageable.getPageNumber() * pageable.getPageSize()).take(pageable.getPageSize());
    }

    @Override
    public Mono<User> save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Mono<User> deleteById(UUID uuid) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        userRepository.findById(uuid).flatMap(u -> {
                    if (u.getUsername().equals(currentUsername)) {
                        SecurityContextHolder.getContext().setAuthentication(null);
                        return userRepository.deleteById(uuid);
                    }
                    else throw new DifferentUserException("Users are forbidden from deleting other Users!");
                }
        );
        return null;
    }

    @Override
    public Mono<User> patchUsername(UUID uuid, String username) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        userRepository.findById(uuid).flatMap(u -> {
            if (u.getUsername().equals(currentUsername)) {
                u.setUsername(username);
                SecurityContextHolder.getContext().setAuthentication(null);
                return userRepository.save(u);
            }
            else throw new DifferentUserException("Users are forbidden from changing other Users!");
        });
        return null;
    }
}
