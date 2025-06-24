package ua.foxminded.carrestservice.dto.update;

import java.util.Set;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object (DTO) for updating a car in the car rest service system.
 * Extends {@link AbstractUpdateDto} to inherit a unique identifier. Contains car details such
 * as production year, model ID, and category IDs for update operations. Uses Lombok annotations
 * to reduce boilerplate code for getters, setters, constructors, and toString generation.
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
public class CarUpdateDto extends AbstractUpdateDto {

    /**
     * The production year of the car to be updated.
     */
    private Integer productionYear;

    /**
     * The ID of the car model associated with the car.
     */
    private Long modelId;

    /**
     * The set of category IDs associated with the car.
     */
    private Set<Long> categoryIds;

}
