package ua.foxminded.carrestservice.service.impl;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.carrestservice.dto.create.CarCreateDto;
import ua.foxminded.carrestservice.dto.response.CarResponseDto;
import ua.foxminded.carrestservice.dto.update.CarUpdateDto;
import ua.foxminded.carrestservice.entity.Car;
import ua.foxminded.carrestservice.mapper.BaseMapper;
import ua.foxminded.carrestservice.repository.CarRepository;
import ua.foxminded.carrestservice.service.CarService;

/**
 * Service implementation for managing {@link Car} entities in the car rest service system.
 * Extends {@link AbstractService} and implements {@link CarService} to provide CRUD operations
 * for {@link CarCreateDto}, {@link CarUpdateDto}, and {@link CarResponseDto}. Includes a method
 * for retrieving cars by model ID with pagination. Uses {@link CarRepository} for data access
 * and {@link BaseMapper} for DTO-entity mappings. Annotated with {@code @Service} for Spring
 * integration, {@code @Slf4j} for logging, and {@code @Validated} for input validation.
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
public class CarServiceImpl extends AbstractService<Car, CarCreateDto, CarUpdateDto,
    CarResponseDto> implements CarService {

    /**
     * Repository for accessing and managing {@link Car} entities.
     */
    private final CarRepository carRepository;

    /**
     * Constructs a {@code CarServiceImpl} with the specified repository and mapper.
     *
     * @param repository the {@link JpaRepository} for {@link Car} entities
     * @param mapper     the {@link BaseMapper} for DTO-entity conversions
     */
    public CarServiceImpl(JpaRepository<Car, Long> repository, BaseMapper<Car, CarCreateDto, CarUpdateDto,
        CarResponseDto> mapper) {
        super(repository, mapper);
        this.carRepository = (CarRepository) repository;
    }

    /**
     * Retrieves a paginated list of cars by model ID or all cars if the ID is null.
     * Uses {@link CarRepository} to query cars and maps results to {@link CarResponseDto}.
     * Executes as a read-only transaction.
     *
     * @param modelId  the ID of the model to filter cars, or null for all cars
     * @param pageable the pagination and sorting configuration
     * @return a paginated list of {@link CarResponseDto}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CarResponseDto> findCarsByModelId(Long modelId, Pageable pageable) {
        log.info("Fetching cars by modelId: {}, pagination: page={}, size={}, sort={}",
            modelId != null ? modelId : "all", pageable.getPageNumber(), pageable.getPageSize(),
            pageable.getSort());
        Page<Car> carsPage = Objects.isNull(modelId)
            ? carRepository.findAll(pageable)
            : carRepository.findByModelId(modelId, pageable);

        if (carsPage.isEmpty()) {
            log.debug("No cars found for modelId: {}, page: {}, size: {}",
                modelId != null ? modelId : "all", pageable.getPageNumber(), pageable.getPageSize());
        } else {
            log.debug("Retrieved {} cars on page {} of {} for modelId: {}, total elements: {}",
                carsPage.getContent().size(), pageable.getPageNumber(),
                carsPage.getTotalPages(), modelId != null ? modelId : "all", carsPage.getTotalElements());
        }

        return carsPage.map(mapper::toResponseDto);
    }

}
