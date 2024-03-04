package com.kaloyan.notetakingapp.controller;

import com.kaloyan.notetakingapp.dto.UserDTO;
import com.kaloyan.notetakingapp.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserServiceImpl userService;
    
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
    public Mono<UserDTO> addUser(@RequestBody UserDTO userDTO) {
        return userService.save(userDTO);
    }

    @PatchMapping("/{id}")
    public Mono<UserDTO> changeUsername(@PathVariable("id") UUID userId, @RequestBody UserDTO userDTO, Authentication authentication) {
        return userService.patchUsername(userId, userDTO.getUsername(), authentication);
    }

    @DeleteMapping("/{id}")
    public Flux<Void> deleteUser(@PathVariable("id") UUID userId, Authentication authentication) {
        return userService.deleteById(userId, authentication);
    }
}