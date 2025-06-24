package ua.foxminded.carrestservice.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.carrestservice.dto.create.CategoryCreateDto;
import ua.foxminded.carrestservice.dto.response.CategoryResponseDto;
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
    @GetMapping
    public ResponseEntity<Page<CategoryResponseDto>> getPageWithCategories(@PageableDefault Pageable pageable) {
        Page<CategoryResponseDto> categoriesPage = categoryService.getAll(pageable);
        return ResponseEntity.ok(categoriesPage);
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id the ID of the category
     * @return a {@link ResponseEntity} with the {@link CategoryResponseDto}
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Long id) {
        CategoryResponseDto category = categoryService.getById(id);
        return ResponseEntity.ok(category);
    }

    /**
     * Creates a new category.
     *
     * @param createDto the {@link CategoryCreateDto} containing category data
     * @return a {@link ResponseEntity} with the created {@link CategoryResponseDto} and location URI
     */
    @PostMapping
    public ResponseEntity<CategoryResponseDto> createNewCategory(@RequestBody CategoryCreateDto createDto) {
        CategoryResponseDto createdCategory = categoryService.save(createDto);
        URI location = URI.create(CATEGORY_LOCATION_URI.formatted(createdCategory.getId()));
        return ResponseEntity.created(location)
            .body(createdCategory);
    }

    /**
     * Updates an existing category.
     *
     * @param updateDto the {@link CategoryUpdateDto} containing updated category data
     * @return a {@link ResponseEntity} with the updated {@link CategoryResponseDto}
     */
    @PutMapping
    public ResponseEntity<CategoryResponseDto> updateCategory(@RequestBody CategoryUpdateDto updateDto) {
        CategoryResponseDto updatedCategory = categoryService.update(updateDto);
        return ResponseEntity.ok(updatedCategory);
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id the ID of the category to delete
     * @return a {@link ResponseEntity} with no content
     */
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent()
            .build();
    }

}
