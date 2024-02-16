package com.kaloyan.notetakingapp.controller;

import com.kaloyan.notetakingapp.model.User;
import org.springframework.data.domain.Pageable;
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

    //Change user's username. Checks if the currently logged-in user is the same that is being changed. If not returns 403 Forbidden
    @PatchMapping("/{id}")
    public Mono<User> changeUsername(@PathVariable("id") UUID userId, @RequestBody String username) {
        return Mono.empty();
    }

    //Delete user. Checks if the currently logged-in user is the same that is being deleted. If not returns 403 Forbidden
    @DeleteMapping("/{id}")
    public Mono<User> deleteUser(@PathVariable("id") UUID userId) {
        return Mono.empty();
    }
}
