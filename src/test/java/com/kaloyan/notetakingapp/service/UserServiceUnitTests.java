package com.kaloyan.notetakingapp.service;

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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceUnitTests {

    private final UserRepository userRepository = mock(UserRepository.class);;

    private final NoteRepository noteRepository = mock(NoteRepository.class);;

    private final Authentication authentication = mock(Authentication.class);

    @InjectMocks
    private UserServiceImpl userService;

    private UUID userId;

    private User user;

    @BeforeAll
    public void init() {
        userId = UUID.randomUUID();
        user = User.builder().id(userId).email("email").username("username").password("password").notes(new ArrayList<>()).build();

        //Mocks
        Mockito.when(userRepository.findById(userId)).thenReturn(Mono.just(user));
        Mockito.when(noteRepository.findByUserId(userId)).thenReturn(Flux.fromIterable(new ArrayList<>()));
    }

    @Test
    public void findByIdReturnsUserDTO(){
        Mono<UserDTO> returnedUser = userService.findById(userId);
        StepVerifier
                .create(returnedUser).expectNextMatches(userDTO -> userDTO.getId().equals(user.getId()))
                .verifyComplete();
    }

    @Test
    public void changingUsernameByDifferentUserThrowsException(){
        Mockito.when(authentication.getName()).thenReturn("differentUsername");

        Mono<UserDTO> returned = userService.patchUsername(user.getId(),"user",authentication);
        StepVerifier.create(returned).expectErrorMatches(throwable -> throwable instanceof DifferentUserException).verify();
    }

    @Test
    public void changingUsernameReturnsUpdatedUserDTO(){
        Mockito.when(authentication.getName()).thenReturn("username");
        Mockito.when(userRepository.save(any(User.class))).thenAnswer(i -> Mono.just(i.getArguments()[0]));

        Mono<UserDTO> returnedUserDTO = userService.patchUsername(userId,"newUsername", authentication);
        StepVerifier.create(returnedUserDTO).expectNextMatches(userDTO -> userDTO.getUsername().equals("newUsername")).verifyComplete();
        user.setUsername("username");
    }

    @Test
    public void deletingUserByDifferentUserThrowsException(){
        Mockito.when(authentication.getName()).thenReturn("differentUsername");

        Flux<Void> returned = userService.deleteById(user.getId(),authentication);
        StepVerifier.create(returned).expectErrorMatches(throwable -> throwable instanceof DifferentUserException).verify();
    }
}
