package ua.foxminded.carrestservice.dto.update;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Abstract base class for DTOs used in update operations in the car rest service system.
 * Provides a common structure for update-specific data transfer objects, including a unique
 * identifier. Uses Lombok annotations to reduce boilerplate code for getters, setters,
 * constructors, and toString generation.
 *
 * @author Serhii Bohdan
 * @see lombok.Getter
 * @see lombok.Setter
 * @see lombok.AllArgsConstructor
 * @see lombok.NoArgsConstructor
 * @see lombok.ToString
 * @see lombok.experimental.SuperBuilder
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder
public abstract class AbstractUpdateDto {

    /**
     * The unique identifier for the entity to be updated.
     */
    private Long id;

}
