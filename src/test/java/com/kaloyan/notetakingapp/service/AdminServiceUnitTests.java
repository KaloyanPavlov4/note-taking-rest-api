package com.kaloyan.notetakingapp.service;

import com.kaloyan.notetakingapp.dto.UserDTO;
import com.kaloyan.notetakingapp.model.Role;
import com.kaloyan.notetakingapp.model.User;
import com.kaloyan.notetakingapp.repository.NoteRepository;
import com.kaloyan.notetakingapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.UUID;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AdminServiceUnitTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    private UUID userId;

    private UUID noteId;

    private User user;

    private User admin;

    @BeforeAll
    public void init(){
        userId = UUID.randomUUID();
        noteId = UUID.randomUUID();
        user = User.builder().id(userId).role(Role.ROLE_USER).notes(new ArrayList<>()).build();
        admin = User.builder().role(Role.ROLE_ADMIN).build();
    }

    @Test
    public void makingUserAdminReturnsEditedUser(){
        Mockito.when(userRepository.findById(userId)).thenReturn(Mono.just(user));
        Mockito.when(userRepository.save(user)).thenReturn(Mono.just(user));
        Mono<UserDTO> returned = adminService.makeUserAdmin(userId);
        StepVerifier.create(returned).expectNextMatches(userDTO -> userDTO.getRole().equals(Role.ROLE_ADMIN)).verifyComplete();
    }

    @Test
    public void deletingUserReturnsMonoVoid(){
        Mockito.when(noteRepository.deleteByUserId(userId)).thenReturn(Mono.empty());
        Mockito.when(userRepository.deleteById(userId)).thenReturn(Mono.empty());
        Mono<Void> returned = adminService.deleteUser(userId);
        StepVerifier.create(returned).expectNext().verifyComplete();
    }

    @Test
    public void deletingNoteReturnsMonoVoid(){
        Mockito.when(noteRepository.deleteById(noteId)).thenReturn(Mono.empty());
        Mono<Void> returned = adminService.deleteNote(noteId);
        StepVerifier.create(returned).expectNext().verifyComplete();
    }

    @Test
    public void isNotAdminReturnsTrueIfUserIsNotAdmin(){
        Mockito.when(userRepository.findById(userId)).thenReturn(Mono.just(user));
        Mono<Boolean> isNotAdmin = adminService.isNotAdmin(userId);
        StepVerifier.create(isNotAdmin).expectNextMatches(bool -> bool).verifyComplete();
    }

    @Test
    public void isNotAdminReturnsFalseIfUserIsAdmin(){
        admin.setRole(Role.ROLE_ADMIN);
        Mockito.when(userRepository.findById(userId)).thenReturn(Mono.just(admin));
        Mono<Boolean> isNotAdmin = adminService.isNotAdmin(userId);
        StepVerifier.create(isNotAdmin).expectNextMatches(bool -> !bool).verifyComplete();
    }
}