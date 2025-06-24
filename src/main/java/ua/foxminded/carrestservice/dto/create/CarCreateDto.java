package ua.foxminded.carrestservice.dto.create;

import java.util.Set;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object (DTO) for creating a new car in the car rest service system.
 * Extends {@link AbstractCreateDto} to inherit common DTO structure. Contains car details such
 * as object ID, production year, model ID, and category IDs for creation operations. Uses Lombok
 * annotations to reduce boilerplate code for getters, setters, constructors, and toString generation.
 *
 * @author Serhii Bohdan
 * @see AbstractCreateDto
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
public class CarCreateDto extends AbstractCreateDto {

    /**
     * The unique object identifier for the car to be created.
     */
    private String objectId;

    /**
     * The production year of the car to be created.
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
