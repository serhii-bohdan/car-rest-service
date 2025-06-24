package ua.foxminded.carrestservice.dto.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Abstract base class for response DTOs in the car rest service system.
 * Provides a common structure for response data transfer objects, including a unique identifier.
 * Uses Lombok annotations to reduce boilerplate code for getters, setters, constructors, and
 * toString generation.
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
public abstract class AbstractResponseDto {

    /**
     * The unique identifier for the entity represented by the DTO.
     */
    private Long id;

}
