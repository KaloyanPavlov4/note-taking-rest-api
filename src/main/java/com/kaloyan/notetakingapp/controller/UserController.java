package com.kaloyan.notetakingapp.controller;

import com.kaloyan.notetakingapp.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    //Gets all users paginated
    @GetMapping()
    public Flux<User> users(@RequestParam Pageable pageable) {
        return Flux.empty();
    }

    //Get user by ID
    @GetMapping("/{id}")
    public Mono<User> user(@PathVariable("id") UUID userId) {
        return Mono.empty();
    }

    //Add user
    @PostMapping
    public Mono<User> addUser(@RequestBody User user) {
        return Mono.just(user);
    }

    //Change user's username. Checks if the authenticated user is the same that is being changed. If not returns 401
    @PatchMapping("/{id}")
    public Mono<User> changeUsername(@PathVariable("id") UUID userId, @RequestBody String username, Authentication authentication) {
        return Mono.empty();
    }

    //Delete user. Checks if the authenticated user is the same that is being deleted. If not returns 401
    @DeleteMapping("/{id}")
    public Mono<User> deleteUser(@PathVariable("id") UUID userId,Authentication authentication) {
        return Mono.empty();
    }
}