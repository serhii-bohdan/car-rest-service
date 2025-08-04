package ua.foxminded.carrestservice.controller;

import static ua.foxminded.carrestservice.util.openapi.ErrorSchemaExample.BAD_REQUEST_RESPONSE;
import static ua.foxminded.carrestservice.util.openapi.ErrorSchemaExample.NOT_FOUND_RESPONSE;
import static ua.foxminded.carrestservice.util.openapi.ManufacturerSchemaExample.*;
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
import ua.foxminded.carrestservice.dto.create.ManufacturerCreateDto;
import ua.foxminded.carrestservice.dto.response.ErrorResponseDto;
import ua.foxminded.carrestservice.dto.response.ManufacturerResponseDto;
import ua.foxminded.carrestservice.dto.update.ManufacturerUpdateDto;
import ua.foxminded.carrestservice.entity.Manufacturer;
import ua.foxminded.carrestservice.service.ManufacturerService;

/**
 * REST controller for managing {@link Manufacturer} entities. Provides endpoints for CRUD operations
 * on manufacturers using {@link ManufacturerService}. Mapped to {@code /api/v1/manufacturers}.
 * Annotated with {@code @RestController} for Spring MVC and {@code @RequiredArgsConstructor} for
 * dependency injection.
 *
 * @author Serhii Bohdan
 * @see ManufacturerService
 * @see ManufacturerCreateDto
 * @see ManufacturerUpdateDto
 * @see ManufacturerResponseDto
 * @see org.springframework.web.bind.annotation.RestController
 * @see lombok.RequiredArgsConstructor
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/manufacturers")
@Tag(name = "Manufacturer Management", description = "API for managing manufacturer records")
public class ManufacturerController {

    /**
     * URI template for manufacturer resource location.
     */
    private static final String MANUFACTURER_LOCATION_URI = "/api/v1/manufacturers/%s";

    /**
     * Service for handling manufacturer-related business logic.
     */
    private final ManufacturerService manufacturerService;

    /**
     * Retrieves a paginated list of manufacturers.
     *
     * @param pageable pagination and sorting configuration, defaults applied via {@code @PageableDefault}
     * @return a {@link ResponseEntity} with a {@link Page} of {@link ManufacturerResponseDto}
     */
    @Operation(summary = "Get a paginated list of manufacturers",
        description = "Retrieves a paginated list of manufacturers with optional sorting. No authentication required.")
    @ApiResponse(responseCode = "200", description = "Successful response",
        content = @Content(schema = @Schema(implementation = ManufacturerResponseDto.class, example = MANUFACTURERS_PAGE_RESPONSE),
            mediaType = "application/json"))
    @GetMapping
    public ResponseEntity<Page<ManufacturerResponseDto>> getPageWithManufacturers(
        @ParameterObject @PageableDefault Pageable pageable) {
        Page<ManufacturerResponseDto> manufacturersPage = manufacturerService.getAll(pageable);
        return ResponseEntity.ok(manufacturersPage);
    }

    /**
     * Retrieves a manufacturer by its ID.
     *
     * @param id the ID of the manufacturer
     * @return a {@link ResponseEntity} with the {@link ManufacturerResponseDto}
     */
    @Operation(summary = "Get a manufacturer by ID",
        description = "Retrieves a manufacturer by its ID. No authentication required.")
    @ApiResponse(responseCode = "200", description = "Successful response",
        content = @Content(schema = @Schema(implementation = ManufacturerResponseDto.class, example = SINGLE_MANUFACTURER_RESPONSE),
            mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "Manufacturer not found",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class, example = NOT_FOUND_RESPONSE),
            mediaType = "application/json"))
    @GetMapping("/{id}")
    public ResponseEntity<ManufacturerResponseDto> getManufacturerById(
        @Parameter(description = "ID of the manufacturer") @PathVariable Long id) {
        ManufacturerResponseDto manufacturer = manufacturerService.getById(id);
        return ResponseEntity.ok(manufacturer);
    }

    /**
     * Creates a new manufacturer.
     *
     * @param createDto the {@link ManufacturerCreateDto} containing manufacturer data
     * @return a {@link ResponseEntity} with the created {@link ManufacturerResponseDto} and location URI
     */
    @Operation(summary = "Create a new manufacturer",
        description = "Creates a new manufacturer record. Requires Bearer Access Token.",
        security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201", description = "Manufacturer created successfully",
        content = @Content(schema = @Schema(implementation = ManufacturerResponseDto.class, example = CREATED_MANUFACTURER_RESPONSE),
            mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "Invalid input",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class, example = BAD_REQUEST_RESPONSE),
            mediaType = "application/json"))
    @PostMapping
    public ResponseEntity<ManufacturerResponseDto> createNewManufacturer(@io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(examples = @ExampleObject(value = MANUFACTURER_TO_CREATE)))
                                                                         @Valid @RequestBody ManufacturerCreateDto createDto) {
        ManufacturerResponseDto createdManufacturer = manufacturerService.save(createDto);
        URI location = URI.create(MANUFACTURER_LOCATION_URI.formatted(createdManufacturer.getId()));
        return ResponseEntity.created(location).body(createdManufacturer);
    }

    /**
     * Updates an existing manufacturer.
     *
     * @param updateDto the {@link ManufacturerUpdateDto} containing updated manufacturer data
     * @return a {@link ResponseEntity} with the updated {@link ManufacturerResponseDto}
     */
    @Operation(summary = "Update an existing manufacturer",
        description = "Updates an existing manufacturer record. Requires Bearer Access Token.",
        security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Manufacturer updated successfully",
        content = @Content(schema = @Schema(implementation = ManufacturerResponseDto.class, example = UPDATED_MANUFACTURER_RESPONSE),
            mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "Invalid input",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class, example = BAD_REQUEST_RESPONSE),
            mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "Manufacturer not found",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class, example = NOT_FOUND_RESPONSE),
            mediaType = "application/json"))
    @PutMapping
    public ResponseEntity<ManufacturerResponseDto> updateManufacturer(@io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(examples = @ExampleObject(value = MANUFACTURER_TO_UPDATE)))
                                                                      @Valid @RequestBody ManufacturerUpdateDto updateDto) {
        ManufacturerResponseDto updatedManufacturer = manufacturerService.update(updateDto);
        return ResponseEntity.ok(updatedManufacturer);
    }

    /**
     * Deletes a manufacturer by its ID.
     *
     * @param id the ID of the manufacturer to delete
     * @return a {@link ResponseEntity} with no content
     */
    @Operation(summary = "Delete a manufacturer by ID",
        description = "Deletes a manufacturer by its ID. Requires Bearer Access Token.",
        security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "204", description = "Manufacturer deleted successfully")
    @ApiResponse(responseCode = "404", description = "Manufacturer not found",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class, example = NOT_FOUND_RESPONSE),
            mediaType = "application/json"))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManufacturerById(
        @Parameter(description = "ID of the manufacturer to delete") @PathVariable Long id) {
        manufacturerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
