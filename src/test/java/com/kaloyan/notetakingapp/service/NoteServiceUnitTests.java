package com.kaloyan.notetakingapp.service;

import com.kaloyan.notetakingapp.config.SecurityUtils;
import com.kaloyan.notetakingapp.dto.NoteDTO;
import com.kaloyan.notetakingapp.exception.DifferentUserException;
import com.kaloyan.notetakingapp.model.Note;
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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest()
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NoteServiceUnitTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteServiceImpl noteService;

    private UUID noteId;

    private UUID userId;

    private Note note;

    private User user;

    @BeforeAll
    public void init(){
        noteId = UUID.randomUUID();
        userId = UUID.randomUUID();
        user = User.builder().id(userId).email("email").username("username").password("password").build();
        note = Note.builder().id(noteId).title("title").text("text").userId(userId).user(user).build();
    }

    @Test
    public void findByIdReturnsNoteDTO(){
        Mockito.when(noteRepository.findById(noteId)).thenReturn(Mono.just(note));
        Mockito.when(userRepository.findById(userId)).thenReturn(Mono.just(user));

        Mono<NoteDTO> returnedNote = noteService.findById(noteId);
        StepVerifier
                .create(returnedNote).expectNextMatches(noteDTO -> noteDTO.getId().equals(note.getId()))
                .verifyComplete();
    }

    @Test
    public void editingNoteReturnsEditedNoteDTO(){
        try(MockedStatic<SecurityUtils> utilities = Mockito.mockStatic(SecurityUtils.class)){
            utilities.when(SecurityUtils::authenticatedUsername).thenReturn(Mono.just("username"));
            Mockito.when(userRepository.findByUsername("username")).thenReturn(Mono.just(user));
            Mockito.when(noteRepository.save(any(Note.class))).thenAnswer(i -> Mono.just(i.getArguments()[0]));
            Mockito.when(noteRepository.findById(noteId)).thenReturn(Mono.just(note));

            NoteDTO editedNote = new NoteDTO();
            editedNote.setTitle("newTitle");
            editedNote.setText("newText");
            Mono<NoteDTO> returnedNote = noteService.edit(noteId,editedNote);
            StepVerifier
                    .create(returnedNote).consumeNextWith(noteDTO -> {
                        assert(noteDTO.getId().equals(noteId));
                        assert(noteDTO.getTitle().equals("newTitle"));
                        assert(noteDTO.getText().equals("newText"));
                        assert(noteDTO.getUserId().equals(userId));
                    })
                    .verifyComplete();
        }
    }

    @Test
    public void editingNoteByDifferentUserThrowsException(){
        try(MockedStatic<SecurityUtils> utilities = Mockito.mockStatic(SecurityUtils.class)){
            utilities.when(SecurityUtils::authenticatedUsername).thenReturn(Mono.just("differentUsername"));
            Mockito.when(userRepository.findByUsername("differentUsername")).thenReturn(Mono.just(user));
            Mockito.when(noteRepository.findById(noteId)).thenReturn(Mono.just(note));

            Mono<NoteDTO> returnedNote = noteService.edit(noteId,new NoteDTO());
            StepVerifier
                    .create(returnedNote).expectErrorMatches(throwable -> throwable instanceof DifferentUserException).verify();
        }
    }

    @Test
    public void deletingNoteByDifferentUserThrowsException(){
        try(MockedStatic<SecurityUtils> utilities = Mockito.mockStatic(SecurityUtils.class)){
            utilities.when(SecurityUtils::authenticatedUsername).thenReturn(Mono.just("differentUsername"));
            Mockito.when(noteRepository.findById(noteId)).thenReturn(Mono.just(note));
            Mockito.when(userRepository.findById(userId)).thenReturn(Mono.just(user));

            Mono<Void> returned = noteService.deleteById(noteId);
            StepVerifier
                    .create(returned).expectErrorMatches(throwable -> throwable instanceof DifferentUserException).verify();
        }
    }
}
