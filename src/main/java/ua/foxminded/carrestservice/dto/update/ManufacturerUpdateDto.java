package ua.foxminded.carrestservice.dto.update;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object (DTO) for updating a manufacturer in the car rest service system.
 * Extends {@link AbstractUpdateDto} to inherit a unique identifier. Contains the manufacturer's
 * name for update operations. Uses Lombok annotations to reduce boilerplate code for getters,
 * setters, constructors, and toString generation.
 *
 * @author Serhii Bohdan
 * @see AbstractUpdateDto
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
public class ManufacturerUpdateDto extends AbstractUpdateDto {

    /**
     * The name of the manufacturer to be updated.
     */
    private String name;

}
