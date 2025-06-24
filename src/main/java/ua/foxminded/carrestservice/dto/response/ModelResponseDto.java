package ua.foxminded.carrestservice.dto.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object (DTO) for representing a car model in response operations in the car rest
 * service system. Extends {@link AbstractResponseDto} to inherit a unique identifier. Contains the
 * model's name and associated manufacturer details. Uses Lombok annotations to reduce boilerplate
 * code for getters, setters, constructors, and toString generation.
 *
 * @author Serhii Bohdan
 * @see AbstractResponseDto
 * @see ManufacturerResponseDto
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
@ToString(callSuper = true)
@SuperBuilder
public class ModelResponseDto extends AbstractResponseDto {

    /**
     * The name of the car model.
     */
    private String name;

    /**
     * The manufacturer associated with the car model.
     */
    private ManufacturerResponseDto manufacturer;

}
