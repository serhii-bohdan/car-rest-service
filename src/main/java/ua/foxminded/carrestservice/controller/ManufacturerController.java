package ua.foxminded.carrestservice.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.carrestservice.dto.create.ManufacturerCreateDto;
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
    @GetMapping
    public ResponseEntity<Page<ManufacturerResponseDto>> getPageWithManufacturers(@PageableDefault Pageable pageable) {
        Page<ManufacturerResponseDto> manufacturersPage = manufacturerService.getAll(pageable);
        return ResponseEntity.ok(manufacturersPage);
    }

    /**
     * Retrieves a manufacturer by its ID.
     *
     * @param id the ID of the manufacturer
     * @return a {@link ResponseEntity} with the {@link ManufacturerResponseDto}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ManufacturerResponseDto> getManufacturerById(@PathVariable Long id) {
        ManufacturerResponseDto manufacturer = manufacturerService.getById(id);
        return ResponseEntity.ok(manufacturer);
    }

    /**
     * Creates a new manufacturer.
     *
     * @param createDto the {@link ManufacturerCreateDto} containing manufacturer data
     * @return a {@link ResponseEntity} with the created {@link ManufacturerResponseDto} and location URI
     */
    @PostMapping
    public ResponseEntity<ManufacturerResponseDto> createNewManufacturer(@RequestBody ManufacturerCreateDto createDto) {
        ManufacturerResponseDto createdManufacturer = manufacturerService.save(createDto);
        URI location = URI.create(MANUFACTURER_LOCATION_URI.formatted(createdManufacturer.getId()));
        return ResponseEntity.created(location)
            .body(createdManufacturer);
    }

    /**
     * Updates an existing manufacturer.
     *
     * @param updateDto the {@link ManufacturerUpdateDto} containing updated manufacturer data
     * @return a {@link ResponseEntity} with the updated {@link ManufacturerResponseDto}
     */
    @PutMapping
    public ResponseEntity<ManufacturerResponseDto> updateManufacturer(@RequestBody ManufacturerUpdateDto updateDto) {
        ManufacturerResponseDto updatedManufacturer = manufacturerService.update(updateDto);
        return ResponseEntity.ok(updatedManufacturer);
    }

    /**
     * Deletes a manufacturer by its ID.
     *
     * @param id the ID of the manufacturer to delete
     * @return a {@link ResponseEntity} with no content
     */
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteManufacturerById(@PathVariable Long id) {
        manufacturerService.deleteById(id);
        return ResponseEntity.noContent()
            .build();
    }

}
