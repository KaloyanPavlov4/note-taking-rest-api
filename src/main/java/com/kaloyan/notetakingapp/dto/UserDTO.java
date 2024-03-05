package com.kaloyan.notetakingapp.dto;

import com.kaloyan.notetakingapp.model.Note;
import com.kaloyan.notetakingapp.model.Role;
import com.kaloyan.notetakingapp.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    private UUID id;
    private String username;
    private String email;
    private String password;
    private Role role;
    private List<NoteDTO> notes = new ArrayList<>();

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole();
        for(Note n : user.getNotes()){
            addNoteDTO(new NoteDTO(n));
        }
    }

    private void addNoteDTO(NoteDTO noteDTO){
        notes.add(noteDTO);
    }
}
