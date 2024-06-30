package com.kaloyan.notetakingapp.controller;

import com.kaloyan.notetakingapp.dto.UserDTO;
import com.kaloyan.notetakingapp.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@Secured("ROLE_ADMIN")
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @PreAuthorize("@adminServiceImpl.isNotAdmin(#userId)")
    @DeleteMapping("/users/{id}")
    public Flux<Void> deleteUser(@PathVariable("id") UUID userId) {
        return adminService.deleteUser(userId);
    }

    @DeleteMapping("/notes/{id}")
    public Mono<Void> deleteNote(@PathVariable("id") UUID noteId) {
        return adminService.deleteNote(noteId);
    }

    @PostMapping("/users/{id}")
    public Mono<UserDTO> makeUserAdmin(@PathVariable("id") UUID userId) {
        return adminService.makeUserAdmin(userId);
    }
}