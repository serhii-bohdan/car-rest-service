package ua.foxminded.carrestservice.service.impl;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.carrestservice.dto.create.ModelCreateDto;
import ua.foxminded.carrestservice.dto.response.ModelResponseDto;
import ua.foxminded.carrestservice.dto.update.ModelUpdateDto;
import ua.foxminded.carrestservice.entity.Model;
import ua.foxminded.carrestservice.mapper.BaseMapper;
import ua.foxminded.carrestservice.repository.ModelRepository;
import ua.foxminded.carrestservice.service.ModelService;

/**
 * Service implementation for managing {@link Model} entities in the car rest service system.
 * Extends {@link AbstractService} and implements {@link ModelService} to provide CRUD operations
 * for {@link ModelCreateDto}, {@link ModelUpdateDto}, and {@link ModelResponseDto}. Includes a
 * method for retrieving models by manufacturer ID with pagination. Uses
 * {@link ModelRepository} for data access and {@link BaseMapper} for DTO-entity mappings.
 * Annotated with {@code @Service} for Spring integration, {@code @Slf4j} for logging, and
 * {@code @Validated} for input validation.
 *
 * @author Serhii Bohdan
 * @see BaseMapper
 * @see lombok.extern.slf4j.Slf4j
 * @see org.springframework.stereotype.Service
 * @see org.springframework.validation.annotation.Validated
 * @see org.springframework.transaction.annotation.Transactional
 */
@Service
@Slf4j
@Validated
public class ModelServiceImpl extends AbstractService<Model, ModelCreateDto, ModelUpdateDto,
    ModelResponseDto> implements ModelService {

    /**
     * Repository for accessing and managing {@link Model} entities.
     */
    private final ModelRepository modelRepository;

    /**
     * Constructs a {@code ModelServiceImpl} with the specified repository and mapper.
     *
     * @param repository the {@link JpaRepository} for {@link Model} entities
     * @param mapper     the {@link BaseMapper} for DTO-entity conversions
     */
    public ModelServiceImpl(JpaRepository<Model, Long> repository, BaseMapper<Model, ModelCreateDto,
        ModelUpdateDto, ModelResponseDto> mapper) {
        super(repository, mapper);
        this.modelRepository = (ModelRepository) repository;
    }

    /**
     * Retrieves a paginated list of models by manufacturer ID or all models if the ID is null.
     * Uses {@link ModelRepository} to query models and maps results to {@link ModelResponseDto}.
     * Executes as a read-only transaction.
     *
     * @param manufacturerId the ID of the manufacturer to filter models, or null for all models
     * @param pageable       the pagination and sorting configuration
     * @return a paginated list of {@link ModelResponseDto}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ModelResponseDto> findModelsByManufacturerId(Long manufacturerId, Pageable pageable) {
        log.info("Fetching models page with manufacturerId: {}, pageable: page={}, size={}, sort={}",
            manufacturerId, pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        Page<ModelResponseDto> page;
        if (Objects.isNull(manufacturerId)) {
            log.debug("manufacturerId is null, fetching all models");
            page = modelRepository.findAll(pageable).map(mapper::toResponseDto);
        } else {
            log.debug("Fetching models for manufacturerId: {}", manufacturerId);
            page = modelRepository.findByManufacturerId(manufacturerId, pageable).map(mapper::toResponseDto);
        }

        log.debug("Returning {} models on page {}/{}",
            page.getNumberOfElements(), page.getNumber() + 1, page.getTotalPages());
        return page;
    }

}
