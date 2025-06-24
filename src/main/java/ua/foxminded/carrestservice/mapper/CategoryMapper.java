package ua.foxminded.carrestservice.mapper;

import org.mapstruct.Mapper;
import ua.foxminded.carrestservice.dto.create.CategoryCreateDto;
import ua.foxminded.carrestservice.dto.response.CategoryResponseDto;
import ua.foxminded.carrestservice.dto.update.CategoryUpdateDto;
import ua.foxminded.carrestservice.entity.Category;

/**
 * MapStruct mapper interface for converting between {@link Category} entities and their DTOs in
 * the car rest service system. Extends {@link BaseMapper} to provide mappings for
 * {@link CategoryCreateDto}, {@link CategoryUpdateDto}, and {@link CategoryResponseDto}.
 * Annotated with {@code @Mapper} to enable Spring integration and automatic implementation.
 *
 * @author Serhii Bohdan
 * @see BaseMapper
 * @see Category
 * @see CategoryCreateDto
 * @see CategoryUpdateDto
 * @see CategoryResponseDto
 * @see org.mapstruct.Mapper
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper extends BaseMapper<Category, CategoryCreateDto, CategoryUpdateDto, CategoryResponseDto> {
}
