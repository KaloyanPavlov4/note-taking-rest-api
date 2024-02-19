package com.kaloyan.notetakingapp.service;

import com.kaloyan.notetakingapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class CustomUserDetailsService implements ReactiveUserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username).map(user -> user);
    }
}