package com.kaloyan.notetakingapp.model;

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
    private String title;
    private String text;
    private UUID userId;
    @Transient
    private User user;

    public Note(UUID id, String title, String text, UUID userId) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.userId = userId;
    }
}
