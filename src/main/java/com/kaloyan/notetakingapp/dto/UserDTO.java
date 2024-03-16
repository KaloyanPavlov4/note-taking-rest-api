package com.kaloyan.notetakingapp.dto;

import com.kaloyan.notetakingapp.model.Note;
import com.kaloyan.notetakingapp.model.Role;
import com.kaloyan.notetakingapp.model.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "Username should not be empty!")
    @Size(min = 3, max = 20, message = "Password should be between 3 and 20 characters!")
    private String username;

    @NotNull(message = "Email should not be empty!")
    @Pattern(regexp = "^.+@\\S+$", flags = {Pattern.Flag.CASE_INSENSITIVE}, message = "Email is not valid!")
    private String email;

    @NotNull(message = "Password should not be empty!")
    @Size(min = 3, max = 20, message = "Password should be between 3 and 20 characters!")
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