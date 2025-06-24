package ua.foxminded.carrestservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.carrestservice.entity.Manufacturer;

/**
 * Spring Data JPA repository for managing {@link Manufacturer} entities in the car rest service system.
 * Extends {@link JpaRepository} to provide CRUD operations for {@link Manufacturer} entities with
 * {@code Long} as the primary key. Annotated with {@code @Repository} to enable Spring's automatic
 * implementation of data access logic.
 *
 * @author Serhii Bohdan
 * @see JpaRepository
 * @see Manufacturer
 * @see org.springframework.stereotype.Repository
 */
@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
}
