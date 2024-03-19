package com.kaloyan.notetakingapp.controller;

import com.kaloyan.notetakingapp.config.SecurityConfig;
import com.kaloyan.notetakingapp.dto.UserDTO;
import com.kaloyan.notetakingapp.model.User;
import com.kaloyan.notetakingapp.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;


@ExtendWith(SpringExtension.class)
@WebFluxTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {SecurityConfig.class, UserController.class})
public class UserControllerUnitTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    private UUID userId;

    @BeforeAll
    public void init() {
        userId = UUID.randomUUID();
    }

    @Test
    public void findByIdReturns200(){
        Mockito.when(userService.findById(userId)).thenReturn(Mono.empty());
        webTestClient.get()
                .uri("/users/" + userId.toString()).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void addUserReturns201(){
        Mockito.when(userService.save(any(UserDTO.class))).thenReturn(Mono.empty());
        webTestClient.post()
                .uri("/users")
                .bodyValue(new UserDTO(User.builder().username("username").password("pass").email("example@email.com").notes(new ArrayList<>()).build()))
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    @WithUserDetails
    public void deletingUserReturns200(){
        Mockito.when(userService.deleteById(any())).thenReturn(Flux.empty());
        webTestClient.delete().uri("/users/" + userId).exchange().expectStatus().isOk();
    }

    @Test
    @WithUserDetails
    public void patchingUsernameReturns200(){
        Mockito.when(userService.patchUsername(any(),any())).thenReturn(Mono.empty());
        webTestClient.patch().uri("/users/" + userId).bodyValue(new HashMap<String,String>()).exchange().expectStatus().isOk();
    }

    @Test
    public void patchingUsernameWithoutAuthenticationReturns401(){
        webTestClient.patch()
                .uri("/users/" + userId)
                .bodyValue(new UserDTO())
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    public void deletingUserWithoutAuthenticationReturns401(){
        webTestClient.delete()
                .uri("/users/" + userId)
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }
}