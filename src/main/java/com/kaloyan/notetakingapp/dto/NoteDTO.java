package com.kaloyan.notetakingapp.dto;

import com.kaloyan.notetakingapp.model.Note;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class NoteDTO {

    private UUID id;
    private String title;
    private String text;
    private UUID userId;

    public NoteDTO(Note note){
        this.id = note.getId();
        this.title = note.getTitle();
        this.text = note.getText();
        this.userId = note.getUserId();
    }
}