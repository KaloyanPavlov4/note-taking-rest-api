package com.kaloyan.notetakingapp.service;

import com.kaloyan.notetakingapp.dto.UserDTO;
import com.kaloyan.notetakingapp.model.Role;
import com.kaloyan.notetakingapp.model.User;
import com.kaloyan.notetakingapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.UUID;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AdminServiceUnitTests {

    @MockBean
    private UserRepository userRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    private UUID userId;

    private User user;

    @BeforeAll
    public void init(){
        userId = UUID.randomUUID();
        user = User.builder().id(userId).role(Role.ROLE_USER).notes(new ArrayList<>()).build();
    }

    @Test
    public void deletingAdminThrowsException(){
        Mockito.when(userRepository.findById(userId)).thenReturn(Mono.just(User.builder().role(Role.ROLE_ADMIN).build()));
        Flux<Void> returned = adminService.deleteUser(userId);
        StepVerifier.create(returned).expectErrorMatches(throwable -> throwable instanceof AccessDeniedException);
    }

    @Test
    public void makingUserAdminReturnsEditedUser(){
        Mockito.when(userRepository.findById(userId)).thenReturn(Mono.just(user));
        Mockito.when(userRepository.save(user)).thenReturn(Mono.just(user));
        Mono<UserDTO> returned = adminService.makeUserAdmin(userId);
        StepVerifier.create(returned).expectNextMatches(userDTO -> userDTO.getRole().equals(Role.ROLE_ADMIN)).expectComplete().verify();
    }
}