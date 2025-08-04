package ua.foxminded.carrestservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.carrestservice.dto.create.AbstractCreateDto;
import ua.foxminded.carrestservice.dto.response.AbstractResponseDto;
import ua.foxminded.carrestservice.dto.update.AbstractUpdateDto;
import ua.foxminded.carrestservice.entity.AbstractEntity;
import ua.foxminded.carrestservice.exception.EntityNotFoundException;
import ua.foxminded.carrestservice.mapper.BaseMapper;
import ua.foxminded.carrestservice.service.BaseService;

/**
 * Abstract base class for service implementations in the car rest service system.
 * Implements {@link BaseService} to provide generic CRUD operations for entities extending
 * {@link AbstractEntity} using DTOs: {@link AbstractCreateDto} for creation,
 * {@link AbstractUpdateDto} for updates, and {@link AbstractResponseDto} for responses. Uses
 * {@link JpaRepository} for data access, {@link BaseMapper} for DTO-entity mappings, and
 * {@code @Transactional} for transaction management. Annotated with {@code @Slf4j} for logging
 * and {@code @RequiredArgsConstructor} for dependency injection.
 *
 * @param <E> the entity type extending {@link AbstractEntity}
 * @param <C> the creation DTO type extending {@link AbstractCreateDto}
 * @param <U> the update DTO type extending {@link AbstractUpdateDto}
 * @param <R> the response DTO type extending {@link AbstractResponseDto}
 * @author Serhii Bohdan
 * @see BaseService
 * @see JpaRepository
 * @see BaseMapper
 * @see EntityNotFoundException
 * @see lombok.RequiredArgsConstructor
 * @see lombok.extern.slf4j.Slf4j
 * @see org.springframework.transaction.annotation.Transactional
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractService<E extends AbstractEntity, C extends AbstractCreateDto,
    U extends AbstractUpdateDto, R extends AbstractResponseDto> implements BaseService<C, U, R> {

    /**
     * Error message template for entity not found exceptions.
     */
    private static final String ENTITY_NOT_FOUND_MESSAGE = "Could not find entity with id: %s";

    /**
     * Repository for accessing and managing entities of type {@code E}.
     */
    protected final JpaRepository<E, Long> repository;

    /**
     * Mapper for converting between entities and DTOs.
     */
    protected final BaseMapper<E, C, U, R> mapper;

    /**
     * Saves a new entity using the provided create DTO.
     * Maps the DTO to an entity, saves it via the repository, and returns the saved entity as a
     * response DTO. Executes within a transaction.
     *
     * @param dto the creation DTO
     * @return the saved entity as a response DTO
     */
    @Override
    @Transactional
    public R save(C dto) {
        log.info("Saving new entity with data: {}", dto);
        E entity = mapper.toCreateEntity(dto);
        E savedEntity = repository.save(entity);
        log.debug("Entity saved successfully: {}", savedEntity);
        return mapper.toResponseDto(savedEntity);
    }

    /**
     * Retrieves an entity by its ID.
     * Returns the entity as a response DTO or throws an {@link EntityNotFoundException} if not
     * found. Executes as a read-only transaction.
     *
     * @param id the ID of the entity
     * @return the entity as a response DTO
     * @throws EntityNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public R getById(Long id) {
        log.info("Retrieving entity by id: {}", id);
        return repository.findById(id)
            .map(entity -> {
                log.debug("Entity found: {}", entity);
                return mapper.toResponseDto(entity);
            }).orElseThrow(() -> {
                log.error("Entity with id {} not found", id);
                return new EntityNotFoundException(ENTITY_NOT_FOUND_MESSAGE.formatted(id));
            });
    }

    /**
     * Retrieves a paginated list of all entities.
     * Uses the provided {@link Pageable} for pagination and sorting. Maps entities to response
     * DTOs. Executes as a read-only transaction.
     *
     * @param pageable the pagination and sorting configuration
     * @return a paginated list of response DTOs
     */
    @Override
    @Transactional(readOnly = true)
    public Page<R> getAll(Pageable pageable) {
        log.info("Fetching all entities with pagination: page={}, size={}, sort={}",
            pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        Page<E> entitiesPage = repository.findAll(pageable);

        if (entitiesPage.isEmpty()) {
            log.debug("No entities found for page {} with size {}",
                pageable.getPageNumber(), pageable.getPageSize());
        } else {
            log.debug("Retrieved {} entities on page {} of {}, total elements: {}",
                entitiesPage.getContent().size(), pageable.getPageNumber(),
                entitiesPage.getTotalPages(), entitiesPage.getTotalElements());
        }

        return entitiesPage.map(mapper::toResponseDto);
    }

    /**
     * Updates an existing entity using the provided update DTO.
     * Maps the DTO to the existing entity, saves the changes, and returns the updated entity as a
     * response DTO. Throws an {@link EntityNotFoundException} if the entity is not found.
     * Executes within a transaction.
     *
     * @param dto the update DTO
     * @return the updated entity as a response DTO
     * @throws EntityNotFoundException if the entity is not found
     */
    @Override
    @Transactional
    public R update(U dto) {
        log.info("Updating entity with ID: {}, data: {}", dto.getId(), dto);
        return repository.findById(dto.getId()).map(entity -> {
            E updatedEntity = repository.save(mapper.toUpdateEntity(dto, entity));
            log.debug("Entity with ID {} updated successfully: {}", dto.getId(), updatedEntity);
            return mapper.toResponseDto(updatedEntity);
        }).orElseThrow(() -> {
            log.error("Entity with ID {} not found for update", dto.getId());
            return new EntityNotFoundException(ENTITY_NOT_FOUND_MESSAGE.formatted(dto.getId()));
        });
    }

    /**
     * Deletes an entity by its ID.
     * Removes the entity from the repository or throws an {@link EntityNotFoundException} if not
     * found.
     *
     * @param id the ID of the entity to delete
     * @throws EntityNotFoundException if the entity is not found
     */
    @Override
    public void deleteById(Long id) {
        log.info("Deleting entity by id: {}", id);
        repository.findById(id).ifPresentOrElse(entity -> {
            repository.delete(entity);
            log.debug("Entity with ID {} deleted successfully", id);
        }, () -> {
            log.error("Entity with id {} not found for deletion", id);
            throw new EntityNotFoundException(ENTITY_NOT_FOUND_MESSAGE.formatted(id));
        });
    }

}
