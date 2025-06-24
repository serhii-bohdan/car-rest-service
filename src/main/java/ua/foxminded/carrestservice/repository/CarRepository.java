package ua.foxminded.carrestservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.carrestservice.entity.Car;

/**
 * Spring Data JPA repository for managing {@link Car} entities in the car rest service system.
 * Extends {@link JpaRepository} to provide CRUD operations for {@link Car} entities with
 * {@code Long} as the primary key. Annotated with {@code @Repository} to enable Spring's automatic
 * implementation of data access logic.
 *
 * @author Serhii Bohdan
 * @see JpaRepository
 * @see Car
 * @see org.springframework.stereotype.Repository
 */
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    /**
     * Retrieves a paginated list of cars for a specific car model.
     * Returns a {@link Page} of {@link Car} entities associated with the specified
     * {@code modelId}, using {@code pageable} for pagination and sorting.
     *
     * @param modelId  the ID of the car model to filter cars by
     * @param pageable the pagination and sorting configuration
     * @return a {@link Page} of {@link Car} entities matching the criteria
     */
    Page<Car> findByModelId(Long modelId, Pageable pageable);

}
