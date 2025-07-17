package ua.foxminded.carrestservice.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.foxminded.carrestservice.dto.create.CarCreateDto;
import ua.foxminded.carrestservice.dto.request.CarSearchRequestDto;
import ua.foxminded.carrestservice.dto.response.CarResponseDto;
import ua.foxminded.carrestservice.dto.update.CarUpdateDto;
import ua.foxminded.carrestservice.entity.Car;

/**
 * Service interface for managing {@link Car} entities in the car rest service system. Extends
 * {@link BaseService} to provide CRUD operations for {@link CarCreateDto}, {@link CarUpdateDto},
 * and {@link CarResponseDto}. Includes an additional method for retrieving cars by model ID with
 * pagination.
 *
 * @author Serhii Bohdan
 * @see BaseService
 * @see CarCreateDto
 * @see CarUpdateDto
 * @see CarResponseDto
 * @see org.springframework.data.domain.Page
 * @see org.springframework.data.domain.Pageable
 * @see jakarta.validation.constraints.NotNull
 */
public interface CarService extends BaseService<CarCreateDto, CarUpdateDto, CarResponseDto> {

    /**
     * Retrieves a paginated list of cars based on the specified search criteria.
     * Uses {@link CarSearchRequestDto} to filter cars by manufacturer, model, category, and year range,
     * with pagination and sorting provided by {@code pageable}. Executes as a read-only transaction.
     *
     * @param carSearchRequest the {@link CarSearchRequestDto} containing search criteria
     * @param pageable         the pagination and sorting configuration
     * @return a {@link Page} of {@link CarResponseDto} matching the criteria
     */
    Page<CarResponseDto> findCarsByCriteria(@NotNull CarSearchRequestDto carSearchRequest, @NotNull Pageable pageable);

}
