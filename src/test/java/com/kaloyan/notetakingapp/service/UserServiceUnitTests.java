package com.kaloyan.notetakingapp.service;

import com.kaloyan.notetakingapp.dto.UserDTO;
import com.kaloyan.notetakingapp.model.Note;
import com.kaloyan.notetakingapp.model.User;
import com.kaloyan.notetakingapp.repository.NoteRepository;
import com.kaloyan.notetakingapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
import java.util.List;
import java.util.UUID;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTests {
    @Mock
    UserRepository userRepository;

    @Mock
    NoteRepository noteRepository;

    @InjectMocks
    UserServiceImpl userService;

    static UUID userId;

    static List<Note> noteList;

    @BeforeAll
    public static void init() {
        userId = UUID.randomUUID();
        noteList = new ArrayList<>();
    }

    @Test
    public void findByIdReturnsUserDTO(){
        User user = User.builder().id(userId).email("email").username("username").password("password").build();
        Mockito.when(userRepository.findById(userId)).thenReturn(Mono.just(user));
        Mockito.when(noteRepository.findAllByUser(userId)).thenReturn(Flux.fromIterable(noteList));

        Mono<UserDTO> returnedUser = userService.findById(userId);
        StepVerifier
                .create(returnedUser).expectNextMatches(userDTO -> userDTO.getId().equals(user.getId()))
                .verifyComplete();
    }
}
