package com.kaloyan.notetakingapp.service;

import com.kaloyan.notetakingapp.dto.UserDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service interface for administrative operations.
 * Provides functionality to manage users, notes, and user roles.
 */
public interface AdminService {

    /**
     * Deletes a user with the specified UUID.
     *
     * @param userId the UUID of the user to be deleted
     * @return a {@link Mono} that completes when the user is deleted
     */
    Mono<Void> deleteUser(UUID userId);

    /**
     * Deletes a note with the specified UUID.
     *
     * @param noteId the UUID of the note to be deleted
     * @return a {@link Mono} that completes when the note is deleted
     */
    Mono<Void> deleteNote(UUID noteId);

    /**
     * Grants admin privileges to a user with the specified UUID.
     *
     * @param userId the UUID of the user to be promoted to admin
     * @return a {@link Mono} containing the updated {@link UserDTO} of the user
     */
    Mono<UserDTO> makeUserAdmin(UUID userId);

    /**
     * Determines if a user is not an admin.
     * <p>
     * This method is designed to be used with Spring Security's {@code @PreAuthorize}
     * annotation due to limitations with SpEL when negating a {@link Mono<Boolean>}.
     *
     * @param userId the UUID of the user
     * @return a {@link Mono} emitting {@code true} if the user is not an admin, {@code false} otherwise
     */
    Mono<Boolean> isNotAdmin(UUID userId);
}
