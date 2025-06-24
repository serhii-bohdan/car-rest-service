package ua.foxminded.carrestservice.dto.create;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object (DTO) for creating a new car model in the car rest service system.
 * Extends {@link AbstractCreateDto} to inherit common DTO structure. Contains the model's name
 * and the ID of the associated manufacturer for creation operations. Uses Lombok annotations to
 * reduce boilerplate code for getters, setters, constructors, and toString generation.
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
public class ModelCreateDto extends AbstractCreateDto {

    /**
     * The name of the car model to be created.
     */
    private String name;

    /**
     * The ID of the manufacturer associated with the car model.
     */
    private Long manufacturerId;

}
