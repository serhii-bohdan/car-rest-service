package ua.foxminded.carrestservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.carrestservice.entity.Category;

/**
 * Spring Data JPA repository for managing {@link Category} entities in the car rest service system.
 * Extends {@link JpaRepository} to provide CRUD operations for {@link Category} entities with
 * {@code Long} as the primary key. Annotated with {@code @Repository} to enable Spring's automatic
 * implementation of data access logic.
 *
 * @author Serhii Bohdan
 * @see JpaRepository
 * @see Category
 * @see org.springframework.stereotype.Repository
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
