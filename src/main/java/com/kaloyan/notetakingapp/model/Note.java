package com.kaloyan.notetakingapp.model;

import com.kaloyan.notetakingapp.dto.NoteDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "notes")
public class Note {

    @Id
    private UUID id;

    @NonNull
    private String title;

    @NonNull
    private String text;

    @NonNull
    private UUID userId;

    @Transient
    private User user;

    public Note(NoteDTO noteDTO) {
        this.title = noteDTO.getTitle();
        this.text = noteDTO.getText();
    }
}
