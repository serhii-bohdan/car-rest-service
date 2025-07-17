package ua.foxminded.carrestservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.carrestservice.entity.Model;

/**
 * Spring Data JPA repository for managing {@link Model} entities in the car rest service system.
 * Extends {@link JpaRepository} to provide CRUD operations for {@link Model} entities with
 * {@code Long} as the primary key. Annotated with {@code @Repository} to enable Spring's automatic
 * implementation of data access logic.
 *
 * @author Serhii Bohdan
 * @see JpaRepository
 * @see Model
 * @see org.springframework.stereotype.Repository
 */
@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {

    /**
     * Retrieves a paginated list of car models for a specific manufacturer.
     * Returns a {@link Page} of {@link Model} entities associated with the specified
     * {@code manufacturerId}, using {@code pageable} for pagination and sorting.
     *
     * @param manufacturerId the ID of the manufacturer to filter models by
     * @param pageable       the pagination and sorting configuration
     * @return a {@link Page} of {@link Model} entities matching the criteria
     */
    Page<Model> findByManufacturerId(Long manufacturerId, Pageable pageable);

    /**
     * Checks if a {@link Model} entity exists with the specified ID.
     *
     * @param id the ID to check for existence
     * @return {@code true} if a model with the given ID exists, {@code false} otherwise
     */
    boolean existsById(long id);

    /**
     * Checks if a {@link Model} entity exists with the specified manufacturer ID and name.
     *
     * @param manufacturerId the ID of the manufacturer
     * @param name           the name of the model
     * @return {@code true} if a model with the given manufacturer ID and name exists,
     * {@code false} otherwise
     */
    boolean existsByManufacturerIdAndName(long manufacturerId, String name);

    /**
     * Checks if a {@link Model} entity exists with the specified manufacturer ID and name,
     * excluding the model with the specified ID.
     *
     * @param manufacturerId the ID of the manufacturer
     * @param name           the name of the model
     * @param id             the ID to exclude from the check
     * @return {@code true} if a model with the given manufacturer ID and name exists
     * (excluding the specified ID), {@code false} otherwise
     */
    boolean existsByManufacturerIdAndNameAndIdIsNot(long manufacturerId, String name, long id);

}
