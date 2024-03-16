package com.kaloyan.notetakingapp.dto;

import com.kaloyan.notetakingapp.model.Note;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class NoteDTO {

    private UUID id;

    @NotNull(message = "Title should not be empty!")
    private String title;

    @Size(max = 5000, message = "Text should not be more than 5000 characters long!")
    private String text;

    private UUID userId;

    public NoteDTO(Note note){
        this.id = note.getId();
        this.title = note.getTitle();
        this.text = note.getText();
        this.userId = note.getUserId();
    }
}