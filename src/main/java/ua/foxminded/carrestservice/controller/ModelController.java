package ua.foxminded.carrestservice.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.carrestservice.dto.create.ModelCreateDto;
import ua.foxminded.carrestservice.dto.response.ModelResponseDto;
import ua.foxminded.carrestservice.dto.update.ModelUpdateDto;
import ua.foxminded.carrestservice.entity.Model;
import ua.foxminded.carrestservice.service.ModelService;

/**
 * REST controller for managing {@link Model} entities. Provides endpoints for CRUD operations on
 * models and retrieval by manufacturer ID using {@link ModelService}. Mapped to {@code /api/v1/models}.
 * Annotated with {@code @RestController} for Spring MVC and {@code @RequiredArgsConstructor} for dependency
 * injection.
 *
 * @author Serhii Bohdan
 * @see ModelService
 * @see ModelCreateDto
 * @see ModelUpdateDto
 * @see ModelResponseDto
 * @see org.springframework.web.bind.annotation.RestController
 * @see lombok.RequiredArgsConstructor
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/models")
public class ModelController {

    /**
     * URI template for model resource location.
     */
    private static final String MODEL_LOCATION_URI = "/api/v1/models/%s";

    /**
     * Service for handling model-related business logic.
     */
    private final ModelService modelService;

    /**
     * Retrieves a paginated list of models, optionally filtered by manufacturer ID.
     *
     * @param manufacturerId the ID of the manufacturer to filter models, or null for all models
     * @param pageable       pagination and sorting configuration, defaults via {@code @PageableDefault}
     * @return a {@link ResponseEntity} with a {@link Page} of {@link ModelResponseDto}
     */
    @GetMapping
    public ResponseEntity<Page<ModelResponseDto>> getPageWithModels(@RequestParam(required = false, value = "manufacturerId") Long manufacturerId,
                                                                    @PageableDefault Pageable pageable) {
        Page<ModelResponseDto> modelsPage = modelService.findModelsByManufacturerId(manufacturerId, pageable);
        return ResponseEntity.ok(modelsPage);
    }

    /**
     * Retrieves a model by its ID.
     *
     * @param id the ID of the model
     * @return a {@link ResponseEntity} with the {@link ModelResponseDto}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ModelResponseDto> getModelById(@PathVariable Long id) {
        ModelResponseDto model = modelService.getById(id);
        return ResponseEntity.ok(model);
    }

    /**
     * Creates a new model.
     *
     * @param createDto the {@link ModelCreateDto} containing model data
     * @return a {@link ResponseEntity} with the created {@link ModelResponseDto} and location URI
     */
    @PostMapping
    public ResponseEntity<ModelResponseDto> createNewModel(@RequestBody ModelCreateDto createDto) {
        ModelResponseDto createdModel = modelService.save(createDto);
        URI location = URI.create(MODEL_LOCATION_URI.formatted(createdModel.getId()));
        return ResponseEntity.created(location)
            .body(createdModel);
    }

    /**
     * Updates an existing model.
     *
     * @param updateDto the {@link ModelUpdateDto} containing updated model data
     * @return a {@link ResponseEntity} with the updated {@link ModelResponseDto}
     */
    @PutMapping
    public ResponseEntity<ModelResponseDto> updateModel(@RequestBody ModelUpdateDto updateDto) {
        ModelResponseDto updatedModel = modelService.update(updateDto);
        return ResponseEntity.ok(updatedModel);
    }

    /**
     * Deletes a model by its ID.
     *
     * @param id the ID of the model to delete
     * @return a {@link ResponseEntity} with no content
     */
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteModelById(@PathVariable Long id) {
        modelService.deleteById(id);
        return ResponseEntity.noContent()
            .build();
    }

}
