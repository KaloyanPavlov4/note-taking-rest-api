package com.kaloyan.notetakingapp.service;

import com.kaloyan.notetakingapp.dto.UserDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserService {

    /**
     * Retrieves a user by their UUID.
     *
     * @param uuid the UUID of the user to be retrieved
     * @return a {@link Mono} containing the {@link UserDTO} with the specified UUID, or empty if not found
     */
    Mono<UserDTO> findById(UUID uuid);

    /**
     * Retrieves all users with pagination support.
     *
     * @param pageable the {@link Pageable} object specifying pagination information
     * @return a {@link Flux} containing {@link UserDTO}s for the requested page
     */
    Flux<UserDTO> findAll(Pageable pageable);

    /**
     * Saves a new user or updates an existing user.
     *
     * @param userDTO the {@link UserDTO} to be saved or updated
     * @return a {@link Mono} containing the saved or updated {@link UserDTO}
     */
    Mono<UserDTO> save(UserDTO userDTO);

    /**
     * Updates the username of an existing user identified by their UUID.
     *
     * @param uuid the UUID of the user whose username is to be updated
     * @param username the new username to be set
     * @return a {@link Mono} containing the updated {@link UserDTO} with the new username
     */
    Mono<UserDTO> patchUsername(UUID uuid, String username);

    /**
     * Deletes a user by their UUID.
     *
     * @param uuid the UUID of the user to be deleted
     * @return a {@link Mono} that completes when the user is deleted
     */
    Mono<Void> deleteById(UUID uuid);
}
