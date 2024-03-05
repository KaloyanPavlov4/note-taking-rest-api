package com.kaloyan.notetakingapp.controller;

import com.kaloyan.notetakingapp.config.SecurityUtils;
import com.kaloyan.notetakingapp.dto.UserDTO;
import com.kaloyan.notetakingapp.model.User;
import com.kaloyan.notetakingapp.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserServiceImpl userService;
    
    @GetMapping()
    public Flux<UserDTO> users(@PageableDefault Pageable pageable) {
        return userService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Mono<UserDTO> user(@PathVariable("id") UUID userId) {
        return userService.findById(userId);
    }

    @PostMapping
    public Mono<UserDTO> addUser(@RequestBody User user) {
        return userService.save(user);
    }

    @PatchMapping("/{id}")
    public Mono<UserDTO> changeUsername(@PathVariable("id") UUID userId, @RequestBody User user) {
        return userService.patchUsername(userId, user.getUsername(), SecurityUtils.currentUsername());
    }

    @DeleteMapping("/{id}")
    public Flux<Void> deleteUser(@PathVariable("id") UUID userId) {
        return userService.deleteById(userId, SecurityUtils.currentUsername());
    }
}