package com.kaloyan.notetakingapp.controller;

import com.kaloyan.notetakingapp.dto.UserDTO;
import com.kaloyan.notetakingapp.service.AdminService;
import com.kaloyan.notetakingapp.service.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.spel.spi.ReactiveEvaluationContextExtension;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/users/{id}")
    @PreAuthorize("!@adminServiceImpl.isAdmin(#userId)")
    public Flux<Void> deleteUser(@PathVariable("id") UUID userId) {
        return adminService.deleteUser(userId);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/notes/{id}")
    public Mono<Void> deleteNote(@PathVariable("id") UUID noteId) {
        return adminService.deleteNote(noteId);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/users/{id}")
    public Mono<UserDTO> makeUserAdmin(@PathVariable("id") UUID userId) {
        return adminService.makeUserAdmin(userId);
    }
}
