package ua.foxminded.carrestservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ua.foxminded.carrestservice.dto.create.CategoryCreateDto;
import ua.foxminded.carrestservice.dto.response.CategoryResponseDto;
import ua.foxminded.carrestservice.dto.update.CategoryUpdateDto;
import ua.foxminded.carrestservice.entity.Category;
import ua.foxminded.carrestservice.mapper.BaseMapper;
import ua.foxminded.carrestservice.service.CategoryService;

/**
 * Service implementation for managing {@link Category} entities in the car rest service system.
 * Extends {@link AbstractService} and implements {@link CategoryService} to provide CRUD
 * operations for {@link CategoryCreateDto}, {@link CategoryUpdateDto}, and
 * {@link CategoryResponseDto}. Uses {@link JpaRepository} for data access and
 * {@link BaseMapper} for DTO-entity mappings. Annotated with {@code @Service} for Spring
 * integration, {@code @Slf4j} for logging, and {@code @Validated} for input validation.
 *
 * @author Serhii Bohdan
 * @see JpaRepository
 * @see BaseMapper
 * @see lombok.extern.slf4j.Slf4j
 * @see org.springframework.stereotype.Service
 * @see org.springframework.validation.annotation.Validated
 */
@Service
@Slf4j
@Validated
public class CategoryServiceImpl extends AbstractService<Category, CategoryCreateDto, CategoryUpdateDto,
    CategoryResponseDto> implements CategoryService {

    /**
     * Constructs a {@code CategoryServiceImpl} with the specified repository and mapper.
     *
     * @param repository the {@link JpaRepository} for {@link Category} entities
     * @param mapper     the {@link BaseMapper} for DTO-entity conversions
     */
    public CategoryServiceImpl(JpaRepository<Category, Long> repository, BaseMapper<Category, CategoryCreateDto,
        CategoryUpdateDto, CategoryResponseDto> mapper) {
        super(repository, mapper);
    }

}
