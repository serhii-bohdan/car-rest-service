package ua.foxminded.carrestservice.controller;

import java.net.URI;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.carrestservice.dto.create.CarCreateDto;
import ua.foxminded.carrestservice.dto.request.CarSearchRequestDto;
import ua.foxminded.carrestservice.dto.response.CarResponseDto;
import ua.foxminded.carrestservice.dto.update.CarUpdateDto;
import ua.foxminded.carrestservice.entity.Car;
import ua.foxminded.carrestservice.service.CarService;

/**
 * REST controller for managing {@link Car} entities. Provides endpoints for CRUD operations on
 * cars and retrieval by model ID using {@link CarService}. Mapped to {@code /api/v1/cars}. Annotated
 * with {@code @RestController} for Spring MVC and {@code @RequiredArgsConstructor} for dependency
 * injection.
 *
 * @author Serhii Bohdan
 * @see CarService
 * @see CarCreateDto
 * @see CarUpdateDto
 * @see CarResponseDto
 * @see org.springframework.web.bind.annotation.RestController
 * @see lombok.RequiredArgsConstructor
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cars")
public class CarController {

    /**
     * URI template for car resource location.
     */
    private static final String CAR_LOCATION_URI = "/api/v1/cars/%s";

    /**
     * Service for handling car-related business logic.
     */
    private final CarService carService;

    /**
     * Retrieves a paginated list of cars based on the provided search criteria.
     * Uses {@link CarSearchRequestDto} for filtering and {@link Pageable} for pagination and sorting.
     *
     * @param carSearchRequest the DTO containing search criteria for filtering cars
     * @param pageable         the pagination and sorting configuration
     * @return a {@link ResponseEntity} containing a {@link Page} of {@link CarResponseDto}
     */
    @GetMapping
    public ResponseEntity<Page<CarResponseDto>> getPageWithCars(CarSearchRequestDto carSearchRequest,
                                                                @PageableDefault Pageable pageable) {
        Page<CarResponseDto> carsPage = carService.findCarsByCriteria(carSearchRequest, pageable);
        return ResponseEntity.ok(carsPage);
    }

    /**
     * Retrieves a car by its ID.
     *
     * @param id the ID of the car
     * @return a {@link ResponseEntity} with the {@link CarResponseDto}
     */
    @GetMapping("/{id}")
    public ResponseEntity<CarResponseDto> getCarById(@PathVariable Long id) {
        CarResponseDto car = carService.getById(id);
        return ResponseEntity.ok(car);
    }

    /**
     * Creates a new car.
     *
     * @param createDto the {@link CarCreateDto} containing car data
     * @return a {@link ResponseEntity} with the created {@link CarResponseDto} and location URI
     */
    @PostMapping
    public ResponseEntity<CarResponseDto> createNewCar(@Valid @RequestBody CarCreateDto createDto) {
        CarResponseDto createdCar = carService.save(createDto);
        URI location = URI.create(CAR_LOCATION_URI.formatted(createdCar.getId()));
        return ResponseEntity.created(location)
            .body(createdCar);
    }

    /**
     * Updates an existing car.
     *
     * @param updateDto the {@link CarUpdateDto} containing updated car data
     * @return a {@link ResponseEntity} with the updated {@link CarResponseDto}
     */
    @PutMapping
    public ResponseEntity<CarResponseDto> updateCar(@Valid @RequestBody CarUpdateDto updateDto) {
        CarResponseDto updatedCar = carService.update(updateDto);
        return ResponseEntity.ok(updatedCar);
    }

    /**
     * Deletes a car by its ID.
     *
     * @param id the ID of the car to delete
     * @return a {@link ResponseEntity} with no content
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCarById(@PathVariable Long id) {
        carService.deleteById(id);
        return ResponseEntity.noContent()
            .build();
    }

}
