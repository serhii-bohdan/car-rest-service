package ua.foxminded.carrestservice.dto.create;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Abstract base class for DTOs used in creation operations in the car rest service system.
 * Provides a common structure for create-specific data transfer objects. Uses Lombok annotations
 * to reduce boilerplate code for getters, setters, constructors, and toString generation.
 *
 * @author Serhii Bohdan
 * @see lombok.Getter
 * @see lombok.Setter
 * @see lombok.AllArgsConstructor
 * @see lombok.ToString
 * @see lombok.experimental.SuperBuilder
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
@SuperBuilder
public abstract class AbstractCreateDto {
}
