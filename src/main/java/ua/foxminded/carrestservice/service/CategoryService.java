package ua.foxminded.carrestservice.service;

import ua.foxminded.carrestservice.dto.create.CategoryCreateDto;
import ua.foxminded.carrestservice.dto.response.CategoryResponseDto;
import ua.foxminded.carrestservice.dto.update.CategoryUpdateDto;
import ua.foxminded.carrestservice.entity.Category;

/**
 * Service interface for managing {@link Category} entities in the car rest service system.
 * Extends {@link BaseService} to provide CRUD operations for {@link CategoryCreateDto},
 * {@link CategoryUpdateDto}, and {@link CategoryResponseDto}.
 *
 * @author Serhii Bohdan
 * @see BaseService
 * @see CategoryCreateDto
 * @see CategoryUpdateDto
 * @see CategoryResponseDto
 */
public interface CategoryService extends BaseService<CategoryCreateDto, CategoryUpdateDto, CategoryResponseDto> {
}
