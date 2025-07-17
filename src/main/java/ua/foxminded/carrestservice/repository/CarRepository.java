package ua.foxminded.carrestservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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
public interface CarRepository extends JpaRepository<Car, Long>, JpaSpecificationExecutor<Car> {
}
