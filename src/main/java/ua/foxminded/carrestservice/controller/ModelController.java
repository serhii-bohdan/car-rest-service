package ua.foxminded.carrestservice.controller;

import static ua.foxminded.carrestservice.util.openapi.ErrorSchemaExample.BAD_REQUEST_RESPONSE;
import static ua.foxminded.carrestservice.util.openapi.ErrorSchemaExample.NOT_FOUND_RESPONSE;
import static ua.foxminded.carrestservice.util.openapi.ModelSchemaExample.*;
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
import ua.foxminded.carrestservice.dto.create.ModelCreateDto;
import ua.foxminded.carrestservice.dto.response.ErrorResponseDto;
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
@Tag(name = "Model Management", description = "API for managing model records")
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
    @Operation(summary = "Get a paginated list of models",
        description = "Retrieves a paginated list of models, optionally filtered by manufacturer ID, with optional sorting. No authentication required.")
    @ApiResponse(responseCode = "200", description = "Successful response",
        content = @Content(schema = @Schema(implementation = ModelResponseDto.class, example = MODELS_PAGE_RESPONSE),
            mediaType = "application/json"))
    @GetMapping
    public ResponseEntity<Page<ModelResponseDto>> getPageWithModels(
        @Parameter(description = "ID of the manufacturer to filter models (optional)", example = "1")
        @RequestParam(required = false, value = "manufacturerId") Long manufacturerId,
        @ParameterObject @PageableDefault Pageable pageable) {
        Page<ModelResponseDto> modelsPage = modelService.findModelsByManufacturerId(manufacturerId, pageable);
        return ResponseEntity.ok(modelsPage);
    }

    /**
     * Retrieves a model by its ID.
     *
     * @param id the ID of the model
     * @return a {@link ResponseEntity} with the {@link ModelResponseDto}
     */
    @Operation(summary = "Get a model by ID",
        description = "Retrieves a model by its ID. No authentication required.")
    @ApiResponse(responseCode = "200", description = "Successful response",
        content = @Content(schema = @Schema(implementation = ModelResponseDto.class, example = SINGLE_MODEL_RESPONSE),
            mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "Model not found",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class, example = NOT_FOUND_RESPONSE),
            mediaType = "application/json"))
    @GetMapping("/{id}")
    public ResponseEntity<ModelResponseDto> getModelById(
        @Parameter(description = "ID of the model") @PathVariable Long id) {
        ModelResponseDto model = modelService.getById(id);
        return ResponseEntity.ok(model);
    }

    /**
     * Creates a new model.
     *
     * @param createDto the {@link ModelCreateDto} containing model data
     * @return a {@link ResponseEntity} with the created {@link ModelResponseDto} and location URI
     */
    @Operation(summary = "Create a new model",
        description = "Creates a new model record. Requires Bearer Access Token.",
        security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201", description = "Model created successfully",
        content = @Content(schema = @Schema(implementation = ModelResponseDto.class, example = CREATED_MODEL_RESPONSE),
            mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "Invalid input",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class, example = BAD_REQUEST_RESPONSE),
            mediaType = "application/json"))
    @PostMapping
    public ResponseEntity<ModelResponseDto> createNewModel(@io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(examples = @ExampleObject(value = MODEL_TO_CREATE)))
                                                           @Valid @RequestBody ModelCreateDto createDto) {
        ModelResponseDto createdModel = modelService.save(createDto);
        URI location = URI.create(MODEL_LOCATION_URI.formatted(createdModel.getId()));
        return ResponseEntity.created(location).body(createdModel);
    }

    /**
     * Updates an existing model.
     *
     * @param updateDto the {@link ModelUpdateDto} containing updated model data
     * @return a {@link ResponseEntity} with the updated {@link ModelResponseDto}
     */
    @Operation(summary = "Update an existing model",
        description = "Updates an existing model record. Requires Bearer Access Token.",
        security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Model updated successfully",
        content = @Content(schema = @Schema(implementation = ModelResponseDto.class, example = UPDATED_MODEL_RESPONSE),
            mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "Invalid input",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class, example = BAD_REQUEST_RESPONSE),
            mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "Model not found",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class, example = NOT_FOUND_RESPONSE),
            mediaType = "application/json"))
    @PutMapping
    public ResponseEntity<ModelResponseDto> updateModel(@io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(examples = @ExampleObject(value = MODEL_TO_UPDATE)))
                                                        @Valid @RequestBody ModelUpdateDto updateDto) {
        ModelResponseDto updatedModel = modelService.update(updateDto);
        return ResponseEntity.ok(updatedModel);
    }

    /**
     * Deletes a model by its ID.
     *
     * @param id the ID of the model to delete
     * @return a {@link ResponseEntity} with no content
     */
    @Operation(summary = "Delete a model by ID",
        description = "Deletes a model by its ID. Requires Bearer Access Token.",
        security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "204", description = "Model deleted successfully")
    @ApiResponse(responseCode = "404", description = "Model not found",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class, example = NOT_FOUND_RESPONSE),
            mediaType = "application/json"))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModelById(
        @Parameter(description = "ID of the model to delete") @PathVariable Long id) {
        modelService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
