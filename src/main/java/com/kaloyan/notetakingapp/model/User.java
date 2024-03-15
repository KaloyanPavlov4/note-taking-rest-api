package com.kaloyan.notetakingapp.model;

import com.kaloyan.notetakingapp.dto.UserDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User implements UserDetails {

    @Id
    private UUID id;

    @NonNull
    private String username;

    @NonNull
    private String email;

    @NonNull
    private String password;

    private Role role;

    @Transient
    private List<Note> notes = new ArrayList<>();

    public User(UserDTO userDTO){
        this.username = userDTO.getUsername();
        this.email = userDTO.getEmail();
        this.password = userDTO.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}