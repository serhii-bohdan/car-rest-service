package ua.foxminded.carrestservice.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.foxminded.carrestservice.dto.create.ModelCreateDto;
import ua.foxminded.carrestservice.dto.response.ModelResponseDto;
import ua.foxminded.carrestservice.dto.update.ModelUpdateDto;
import ua.foxminded.carrestservice.entity.Model;

/**
 * Service interface for managing {@link Model} entities in the car rest service system.
 * Extends {@link BaseService} to provide CRUD operations for {@link ModelCreateDto},
 * {@link ModelUpdateDto}, and {@link ModelResponseDto}. Includes an additional method for
 * retrieving models by manufacturer ID with pagination.
 *
 * @author Serhii Bohdan
 * @see BaseService
 * @see ModelCreateDto
 * @see ModelUpdateDto
 * @see ModelResponseDto
 * @see org.springframework.data.domain.Page
 * @see org.springframework.data.domain.Pageable
 * @see jakarta.validation.constraints.NotNull
 */
public interface ModelService extends BaseService<ModelCreateDto, ModelUpdateDto, ModelResponseDto> {

    /**
     * Retrieves a paginated list of models associated with the specified manufacturer ID.
     *
     * @param manufacturerId the ID of the manufacturer to filter models
     * @param pageable       the pagination and sorting configuration
     * @return a paginated list of {@link ModelResponseDto}
     */
    Page<ModelResponseDto> findModelsByManufacturerId(Long manufacturerId, @NotNull Pageable pageable);

}
