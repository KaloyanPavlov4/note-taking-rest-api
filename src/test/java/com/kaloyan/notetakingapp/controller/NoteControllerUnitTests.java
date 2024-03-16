package com.kaloyan.notetakingapp.controller;

import com.kaloyan.notetakingapp.config.SecurityConfig;
import com.kaloyan.notetakingapp.dto.NoteDTO;
import com.kaloyan.notetakingapp.model.Note;
import com.kaloyan.notetakingapp.service.NoteService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@WebFluxTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {SecurityConfig.class, NoteController.class})
public class NoteControllerUnitTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private NoteService noteService;

    UUID noteId;

    @BeforeAll
    public void init() {
        noteId = UUID.randomUUID();
    }

    @Test
    public void findByIdReturns200(){
        Mockito.when(noteService.findById(noteId)).thenReturn(Mono.empty());
        webTestClient.get()
                .uri("/notes/" + noteId.toString())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @WithUserDetails
    public void addNoteReturns201(){
        Mockito.when(noteService.save(any(),any())).thenReturn(Mono.empty());
        webTestClient.post().uri("/notes").bodyValue(new NoteDTO(Note.builder().title("Title").text("Text").build())).exchange().expectStatus().isCreated();
    }

    @Test
    @WithUserDetails
    public void editingNoteReturns200(){
        Mockito.when(noteService.edit(any(),any(),any())).thenReturn(Mono.empty());
        webTestClient.put().uri("/notes/" + noteId).bodyValue(new NoteDTO()).exchange().expectStatus().isOk();
    }

    @Test
    @WithUserDetails
    public void deletingNoteReturns200(){
        Mockito.when(noteService.deleteById(any(),any())).thenReturn(Mono.empty());
        webTestClient.delete().uri("/notes/" + noteId).exchange().expectStatus().isOk();
    }

    @Test
    public void addingNoteWithoutAuthenticationReturns401(){
        webTestClient.post().uri("/notes").bodyValue(new NoteDTO()).exchange().expectStatus().isUnauthorized();
    }

    @Test
    public void editingNoteWithoutAuthenticationReturns401(){
        webTestClient.put().uri("/notes/" + noteId).bodyValue(new NoteDTO()).exchange().expectStatus().isUnauthorized();
    }

    @Test
    public void deletingNoteWithoutAuthenticationReturns401(){
        webTestClient.delete().uri("/notes/" + noteId).exchange().expectStatus().isUnauthorized();
    }

}