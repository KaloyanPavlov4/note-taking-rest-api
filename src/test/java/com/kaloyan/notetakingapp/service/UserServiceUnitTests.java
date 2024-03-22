package com.kaloyan.notetakingapp.service;

import com.kaloyan.notetakingapp.config.SecurityUtils;
import com.kaloyan.notetakingapp.dto.UserDTO;
import com.kaloyan.notetakingapp.exception.DifferentUserException;
import com.kaloyan.notetakingapp.model.User;
import com.kaloyan.notetakingapp.repository.NoteRepository;
import com.kaloyan.notetakingapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceUnitTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UUID userId;

    private User user;

    @BeforeAll
    public void init() {
        userId = UUID.randomUUID();
        user = User.builder().id(userId).email("email").username("username").password("password").notes(new ArrayList<>()).build();
    }

    @Test
    public void findByIdReturnsUserDTO(){
        Mockito.when(userRepository.findById(userId)).thenReturn(Mono.just(user));
        Mockito.when(noteRepository.findByUserId(userId)).thenReturn(Flux.fromIterable(new ArrayList<>()));

        Mono<UserDTO> returnedUser = userService.findById(userId);
        StepVerifier
                .create(returnedUser).expectNextMatches(userDTO -> userDTO.getId().equals(user.getId()))
                .verifyComplete();
    }

    @Test
    public void changingUsernameByDifferentUserThrowsException(){
        try(MockedStatic<SecurityUtils> utilities = Mockito.mockStatic(SecurityUtils.class)){
            utilities.when(SecurityUtils::authenticatedUsername).thenReturn(Mono.just("differentUsername"));
            Mockito.when(userRepository.findById(userId)).thenReturn(Mono.just(user));

            Mono<UserDTO> returned = userService.patchUsername(user.getId(),"user");
            StepVerifier.create(returned).verifyErrorMatches(throwable -> throwable instanceof DifferentUserException);
        }
    }

    @Test
    public void changingUsernameReturnsUpdatedUserDTO(){
        try(MockedStatic<SecurityUtils> utilities = Mockito.mockStatic(SecurityUtils.class)){
            utilities.when(SecurityUtils::authenticatedUsername).thenReturn(Mono.just("username"));
            Mockito.when(userRepository.findById(userId)).thenReturn(Mono.just(user));
            Mockito.when(noteRepository.findByUserId(userId)).thenReturn(Flux.fromIterable(new ArrayList<>()));
            Mockito.when(userRepository.save(any(User.class))).thenAnswer(i -> Mono.just(i.getArguments()[0]));

            Mono<UserDTO> returnedUserDTO = userService.patchUsername(userId,"newUsername");
            StepVerifier.create(returnedUserDTO).expectNextMatches(userDTO -> userDTO.getUsername().equals("newUsername")).verifyComplete();
            user.setUsername("username");
        }
    }

    @Test
    public void deletingUserByDifferentUserThrowsException(){
        try(MockedStatic<SecurityUtils> utilities = Mockito.mockStatic(SecurityUtils.class)){
            utilities.when(SecurityUtils::authenticatedUsername).thenReturn(Mono.just("differentUsername"));
            Mockito.when(userRepository.findById(userId)).thenReturn(Mono.just(user));

            Flux<Void> returned = userService.deleteById(user.getId());
            StepVerifier.create(returned).verifyErrorMatches(throwable -> throwable instanceof DifferentUserException);
        }
    }
}