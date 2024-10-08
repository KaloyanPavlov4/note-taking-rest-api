package com.kaloyan.notetakingapp.controller;

import com.kaloyan.notetakingapp.dto.UserDTO;
import com.kaloyan.notetakingapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public Flux<UserDTO> users(@PageableDefault Pageable pageable) {
        return userService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Mono<UserDTO> user(@PathVariable("id") UUID userId) {
        return userService.findById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserDTO> addUser(@Valid @RequestBody UserDTO user) {
        return userService.save(user);
    }

    @PatchMapping("/{id}")
    public Mono<UserDTO> changeUsername(@PathVariable("id") UUID userId, @RequestBody Map<String, String> username) {
        return userService.patchUsername(userId, username.get("username"));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteUser(@PathVariable("id") UUID userId) {
        return userService.deleteById(userId);
    }
}