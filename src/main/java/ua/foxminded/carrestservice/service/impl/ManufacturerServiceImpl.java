package ua.foxminded.carrestservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.carrestservice.dto.create.ManufacturerCreateDto;
import ua.foxminded.carrestservice.dto.response.ManufacturerResponseDto;
import ua.foxminded.carrestservice.dto.update.ManufacturerUpdateDto;
import ua.foxminded.carrestservice.entity.Manufacturer;
import ua.foxminded.carrestservice.mapper.BaseMapper;
import ua.foxminded.carrestservice.service.ManufacturerService;

/**
 * Service implementation for managing {@link Manufacturer} entities in the car rest service system.
 * Extends {@link AbstractService} and implements {@link ManufacturerService} to provide CRUD
 * operations for {@link ManufacturerCreateDto}, {@link ManufacturerUpdateDto}, and
 * {@link ManufacturerResponseDto}. Uses {@link JpaRepository} for data access and
 * {@link BaseMapper} for DTO-entity mappings. Annotated with {@code @Service} for Spring
 * integration, {@code @Slf4j} for logging, and {@code @Validated} for input validation.
 *
 * @author Serhii Bohdan
 * @see JpaRepository
 * @see BaseMapper
 * @see lombok.extern.slf4j.Slf4j
 * @see org.springframework.stereotype.Service
 * @see org.springframework.validation.annotation.Validated
 */
@Service
@Slf4j
@Validated
public class ManufacturerServiceImpl extends AbstractService<Manufacturer, ManufacturerCreateDto,
    ManufacturerUpdateDto, ManufacturerResponseDto> implements ManufacturerService {

    /**
     * Constructs a {@code ManufacturerServiceImpl} with the specified repository and mapper.
     *
     * @param repository the {@link JpaRepository} for {@link Manufacturer} entities
     * @param mapper     the {@link BaseMapper} for DTO-entity conversions
     */
    public ManufacturerServiceImpl(JpaRepository<Manufacturer, Long> repository, BaseMapper<Manufacturer,
        ManufacturerCreateDto, ManufacturerUpdateDto, ManufacturerResponseDto> mapper) {
        super(repository, mapper);
    }

}
