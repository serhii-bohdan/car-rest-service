package ua.foxminded.carrestservice.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.foxminded.carrestservice.dto.create.AbstractCreateDto;
import ua.foxminded.carrestservice.dto.response.AbstractResponseDto;
import ua.foxminded.carrestservice.dto.update.AbstractUpdateDto;

/**
 * Generic service interface for managing entities in the car rest service system.
 * Defines CRUD operations for entities using DTOs: {@link AbstractCreateDto} for creation,
 * {@link AbstractUpdateDto} for updates, and {@link AbstractResponseDto} for responses. Supports
 * pagination for retrieving multiple entities. Uses {@code @NotNull} for input validation.
 *
 * @param <C> the creation DTO type extending {@link AbstractCreateDto}
 * @param <U> the update DTO type extending {@link AbstractUpdateDto}
 * @param <R> the response DTO type extending {@link AbstractResponseDto}
 * @author Serhii Bohdan
 * @see org.springframework.data.domain.Page
 * @see org.springframework.data.domain.Pageable
 * @see jakarta.validation.constraints.NotNull
 */
public interface BaseService<C extends AbstractCreateDto, U extends AbstractUpdateDto,
    R extends AbstractResponseDto> {

    /**
     * Saves a new entity based on the provided create DTO.
     *
     * @param dto the creation DTO to save
     * @return the saved entity as a response DTO
     */
    R save(@NotNull C dto);

    /**
     * Retrieves an entity by its ID.
     *
     * @param id the ID of the entity to retrieve
     * @return the entity as a response DTO
     */
    R getById(@NotNull Long id);

    /**
     * Retrieves a paginated list of all entities.
     *
     * @param pageable the pagination and sorting configuration
     * @return a paginated list of response DTOs
     */
    Page<R> getAll(@NotNull Pageable pageable);

    /**
     * Updates an existing entity based on the provided update DTO.
     *
     * @param dto the update DTO containing the new data
     * @return the updated entity as a response DTO
     */
    R update(@NotNull U dto);

    /**
     * Deletes an entity by its ID.
     *
     * @param id the ID of the entity to delete
     */
    void deleteById(@NotNull Long id);

}
