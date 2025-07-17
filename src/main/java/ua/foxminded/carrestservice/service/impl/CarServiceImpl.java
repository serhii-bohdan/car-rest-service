package ua.foxminded.carrestservice.service.impl;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.carrestservice.dto.create.CarCreateDto;
import ua.foxminded.carrestservice.dto.request.CarSearchRequestDto;
import ua.foxminded.carrestservice.dto.response.CarResponseDto;
import ua.foxminded.carrestservice.dto.update.CarUpdateDto;
import ua.foxminded.carrestservice.entity.Car;
import ua.foxminded.carrestservice.mapper.BaseMapper;
import ua.foxminded.carrestservice.repository.CarRepository;
import ua.foxminded.carrestservice.service.CarService;
import ua.foxminded.carrestservice.util.specification.CarSpecification;

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
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CarResponseDto> findCarsByCriteria(CarSearchRequestDto carSearchRequest, Pageable pageable) {
        log.info("Filtering cars with parameters: {}", carSearchRequest);
        Specification<Car> specification = buildSpecification(carSearchRequest);
        return carRepository.findAll(specification, pageable).map(mapper::toResponseDto);
    }

    private Specification<Car> buildSpecification(CarSearchRequestDto carSearchRequest) {
        Specification<Car> specification = (root, query, criteriaBuilder) -> null;
        String manufacturer = carSearchRequest.getManufacturer();
        String model = carSearchRequest.getModel();
        String category = carSearchRequest.getCategory();
        Integer minYear = carSearchRequest.getMinYear();
        Integer maxYear = carSearchRequest.getMaxYear();

        if (Objects.nonNull(manufacturer)) {
            specification = specification.and(CarSpecification.withManufacturer(manufacturer));
        }
        if (Objects.nonNull(model)) {
            specification = specification.and(CarSpecification.withModel(model));
        }
        if (Objects.nonNull(category)) {
            specification = specification.and(CarSpecification.withCategory(category));
        }
        if (Objects.nonNull(minYear) && Objects.isNull(maxYear)) {
            specification = specification.and(CarSpecification.withProductionYearGreaterThanOrEqual(minYear));
        }
        if (Objects.isNull(minYear) && Objects.nonNull(maxYear)) {
            specification = specification.and(CarSpecification.withProductionYearLessThanOrEqual(maxYear));
        }
        if (Objects.nonNull(minYear) && Objects.nonNull(maxYear)) {
            specification = specification.and(CarSpecification.withProductionYearBetween(minYear, maxYear));
        }

        log.debug("Specification built: {}", specification);
        return specification;
    }

}
