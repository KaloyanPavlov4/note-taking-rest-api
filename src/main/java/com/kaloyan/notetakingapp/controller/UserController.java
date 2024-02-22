package com.kaloyan.notetakingapp.controller;

import com.kaloyan.notetakingapp.dto.UserDTO;
import com.kaloyan.notetakingapp.model.User;
import com.kaloyan.notetakingapp.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserServiceImpl userService;

    //Gets all users paginated
    @GetMapping()
    public Flux<UserDTO> users(@PageableDefault Pageable pageable) {
        return userService.findAll(pageable);
    }

    //Get user by ID
    @GetMapping("/{id}")
    public Mono<UserDTO> user(@PathVariable("id") UUID userId) {
        return userService.findById(userId);
    }

    //Add user
    @PostMapping
    public Mono<UserDTO> addUser(@RequestBody User user) {
        return userService.save(user);
    }

    //Change user's username. Checks if the authenticated user is the same that is being changed. If not returns 401
    @PatchMapping("/{id}")
    public Mono<UserDTO> changeUsername(@PathVariable("id") UUID userId, @RequestBody User user, Authentication authentication) {
        return userService.patchUsername(userId, user.getUsername(), authentication);
    }

    //Delete user and all notes by them. Checks if the authenticated user is the same that is being deleted. If not returns 401
    @DeleteMapping("/{id}")
    public Flux<Void> deleteUser(@PathVariable("id") UUID userId, Authentication authentication) {
        return userService.deleteById(userId, authentication);
    }
}