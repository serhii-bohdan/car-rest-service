package ua.foxminded.carrestservice.controller;

import static ua.foxminded.carrestservice.util.openapi.CategorySchemaExample.*;
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
import ua.foxminded.carrestservice.dto.create.CategoryCreateDto;
import ua.foxminded.carrestservice.dto.response.CategoryResponseDto;
import ua.foxminded.carrestservice.dto.response.ErrorResponseDto;
import ua.foxminded.carrestservice.dto.update.CategoryUpdateDto;
import ua.foxminded.carrestservice.entity.Category;
import ua.foxminded.carrestservice.service.CategoryService;

/**
 * REST controller for managing {@link Category} entities. Provides endpoints for CRUD operations
 * on categories using {@link CategoryService}. Mapped to {@code /api/v1/categories}. Annotated with
 * {@code @RestController} for Spring MVC and {@code @RequiredArgsConstructor} for dependency injection.
 *
 * @author Serhii Bohdan
 * @see CategoryService
 * @see CategoryCreateDto
 * @see CategoryUpdateDto
 * @see CategoryResponseDto
 * @see org.springframework.web.bind.annotation.RestController
 * @see lombok.RequiredArgsConstructor
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
@Tag(name = "Category Management", description = "API for managing category records")
public class CategoryController {

    /**
     * URI template for category resource location.
     */
    private static final String CATEGORY_LOCATION_URI = "/api/v1/categories/%s";

    /**
     * Service for handling category-related business logic.
     */
    private final CategoryService categoryService;

    /**
     * Retrieves a paginated list of categories.
     *
     * @param pageable pagination and sorting configuration, defaults applied via {@code @PageableDefault}
     * @return a {@link ResponseEntity} with a {@link Page} of {@link CategoryResponseDto}
     */
    @Operation(summary = "Get a paginated list of categories",
        description = "Retrieves a paginated list of categories with optional sorting. No authentication required.")
    @ApiResponse(responseCode = "200", description = "Successful response",
        content = @Content(schema = @Schema(implementation = CategoryResponseDto.class, example = CATEGORIES_PAGE_RESPONSE),
            mediaType = "application/json"))
    @GetMapping
    public ResponseEntity<Page<CategoryResponseDto>> getPageWithCategories(
        @ParameterObject @Parameter(description = "Pagination and sorting parameters (e.g., ?page=0&size=10&sort=name,asc)")
        @PageableDefault Pageable pageable) {
        Page<CategoryResponseDto> categoriesPage = categoryService.getAll(pageable);
        return ResponseEntity.ok(categoriesPage);
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id the ID of the category
     * @return a {@link ResponseEntity} with the {@link CategoryResponseDto}
     */
    @Operation(summary = "Get a category by ID",
        description = "Retrieves a category by its ID. No authentication required.")
    @ApiResponse(responseCode = "200", description = "Successful response",
        content = @Content(schema = @Schema(implementation = CategoryResponseDto.class, example = SINGLE_CATEGORY_RESPONSE),
            mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "Category not found",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class, example = NOT_FOUND_RESPONSE),
            mediaType = "application/json"))
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(
        @Parameter(description = "ID of the category") @PathVariable Long id) {
        CategoryResponseDto category = categoryService.getById(id);
        return ResponseEntity.ok(category);
    }

    /**
     * Creates a new category.
     *
     * @param createDto the {@link CategoryCreateDto} containing category data
     * @return a {@link ResponseEntity} with the created {@link CategoryResponseDto} and location URI
     */
    @Operation(summary = "Create a new category",
        description = "Creates a new category record. Requires Bearer Access Token.",
        security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201", description = "Category created successfully",
        content = @Content(schema = @Schema(implementation = CategoryResponseDto.class, example = CREATED_CATEGORY_RESPONSE),
            mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "Invalid input",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class, example = BAD_REQUEST_RESPONSE),
            mediaType = "application/json"))
    @PostMapping
    public ResponseEntity<CategoryResponseDto> createNewCategory(@io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(examples = @ExampleObject(value = CATEGORY_TO_CREATE)))
                                                                 @Valid @RequestBody CategoryCreateDto createDto) {
        CategoryResponseDto createdCategory = categoryService.save(createDto);
        URI location = URI.create(CATEGORY_LOCATION_URI.formatted(createdCategory.getId()));
        return ResponseEntity.created(location).body(createdCategory);
    }

    /**
     * Updates an existing category.
     *
     * @param updateDto the {@link CategoryUpdateDto} containing updated category data
     * @return a {@link ResponseEntity} with the updated {@link CategoryResponseDto}
     */
    @Operation(summary = "Update an existing category",
        description = "Updates an existing category record. Requires Bearer Access Token.",
        security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Category updated successfully",
        content = @Content(schema = @Schema(implementation = CategoryResponseDto.class, example = UPDATED_CATEGORY_RESPONSE),
            mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "Invalid input",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class, example = BAD_REQUEST_RESPONSE),
            mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "Category not found",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class, example = NOT_FOUND_RESPONSE),
            mediaType = "application/json"))
    @PutMapping
    public ResponseEntity<CategoryResponseDto> updateCategory(@io.swagger.v3.oas.annotations.parameters.RequestBody(content =
    @Content(examples = @ExampleObject(value = CATEGORY_TO_UPDATE)))
                                                              @Valid @RequestBody CategoryUpdateDto updateDto) {
        CategoryResponseDto updatedCategory = categoryService.update(updateDto);
        return ResponseEntity.ok(updatedCategory);
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id the ID of the category to delete
     * @return a {@link ResponseEntity} with no content
     */
    @Operation(summary = "Delete a category by ID",
        description = "Deletes a category by its ID. Requires Bearer Access Token.",
        security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "204", description = "Category deleted successfully")
    @ApiResponse(responseCode = "404", description = "Category not found",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class, example = NOT_FOUND_RESPONSE),
            mediaType = "application/json"))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryById(
        @Parameter(description = "ID of the category to delete") @PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
