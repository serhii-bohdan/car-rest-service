package ua.foxminded.carrestservice.controller;

import static ua.foxminded.carrestservice.util.openapi.CarSchemaExample.*;
import static ua.foxminded.carrestservice.util.openapi.ErrorSchemaExample.BAD_REQUEST_RESPONSE;
import static ua.foxminded.carrestservice.util.openapi.ErrorSchemaExample.NOT_FOUND_RESPONSE;
import java.net.URI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.carrestservice.dto.create.CarCreateDto;
import ua.foxminded.carrestservice.dto.request.CarSearchRequestDto;
import ua.foxminded.carrestservice.dto.response.CarResponseDto;
import ua.foxminded.carrestservice.dto.response.ErrorResponseDto;
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
@Tag(name = "Car Management", description = "API for managing car records")
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
    @Operation(summary = "Get a paginated list of cars",
        description = "Retrieves a paginated list of cars based on search criteria, with optional sorting. No authentication required.")
    @ApiResponse(responseCode = "200", description = "Successful response",
        content = @Content(schema = @Schema(implementation = CarResponseDto.class, example = CARS_PAGE_RESPONSE),
            mediaType = "application/json"))
    @GetMapping
    public ResponseEntity<Page<CarResponseDto>> getPageWithCars(@ParameterObject CarSearchRequestDto carSearchRequest,
                                                                @ParameterObject @PageableDefault Pageable pageable) {
        Page<CarResponseDto> carsPage = carService.findCarsByCriteria(carSearchRequest, pageable);
        return ResponseEntity.ok(carsPage);
    }

    /**
     * Retrieves a car by its ID.
     *
     * @param id the ID of the car
     * @return a {@link ResponseEntity} with the {@link CarResponseDto}
     */
    @Operation(summary = "Get a car by ID",
        description = "Retrieves a car by its ID. No authentication required.")
    @ApiResponse(responseCode = "200", description = "Successful response",
        content = @Content(schema = @Schema(implementation = CarResponseDto.class, example = SINGLE_CAR_RESPONSE),
            mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "Car not found",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class, example = NOT_FOUND_RESPONSE),
            mediaType = "application/json"))
    @GetMapping("/{id}")
    public ResponseEntity<CarResponseDto> getCarById(
        @Parameter(description = "ID of the car") @PathVariable Long id) {
        CarResponseDto car = carService.getById(id);
        return ResponseEntity.ok(car);
    }

    /**
     * Creates a new car.
     *
     * @param createDto the {@link CarCreateDto} containing car data
     * @return a {@link ResponseEntity} with the created {@link CarResponseDto} and location URI
     */
    @Operation(summary = "Create a new car",
        description = "Creates a new car record. Requires Bearer Access Token.",
        security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201", description = "Car created successfully",
        content = @Content(schema = @Schema(implementation = CarResponseDto.class, example = CREATED_CAR_RESPONSE),
            mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "Invalid input",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class, example = BAD_REQUEST_RESPONSE),
            mediaType = "application/json"))
    @PostMapping
    public ResponseEntity<CarResponseDto> createNewCar(@io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(examples = @ExampleObject(value = CAR_TO_CREATE)))
                                                       @Valid @RequestBody CarCreateDto createDto) {
        CarResponseDto createdCar = carService.save(createDto);
        URI location = URI.create(CAR_LOCATION_URI.formatted(createdCar.getId()));
        return ResponseEntity.created(location).body(createdCar);
    }

    /**
     * Updates an existing car.
     *
     * @param updateDto the {@link CarUpdateDto} containing updated car data
     * @return a {@link ResponseEntity} with the updated {@link CarResponseDto}
     */
    @Operation(summary = "Update an existing car",
        description = "Updates an existing car record. Requires Bearer Access Token.",
        security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Car updated successfully",
        content = @Content(schema = @Schema(implementation = CarResponseDto.class, example = UPDATED_CAR_RESPONSE),
            mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "Invalid input",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class, example = BAD_REQUEST_RESPONSE),
            mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "Car not found",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class, example = NOT_FOUND_RESPONSE),
            mediaType = "application/json"))
    @PutMapping
    public ResponseEntity<CarResponseDto> updateCar(@io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(examples = @ExampleObject(value = CAR_TO_UPDATE)))
                                                    @Valid @RequestBody CarUpdateDto updateDto) {
        CarResponseDto updatedCar = carService.update(updateDto);
        return ResponseEntity.ok(updatedCar);
    }

    /**
     * Deletes a car by its ID.
     *
     * @param id the ID of the car to delete
     * @return a {@link ResponseEntity} with no content
     */
    @Operation(summary = "Delete a car by ID",
        description = "Deletes a car by its ID. Requires Bearer Access Token.",
        security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "204", description = "Car deleted successfully")
    @ApiResponse(responseCode = "404", description = "Car not found",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class, example = NOT_FOUND_RESPONSE),
            mediaType = "application/json"))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarById(@Parameter(description = "ID of the car to delete") @PathVariable Long id) {
        carService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
