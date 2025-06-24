package ua.foxminded.carrestservice.dto.response;

import java.util.Set;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object (DTO) for representing a car in response operations in the car rest service
 * system. Extends {@link AbstractResponseDto} to inherit a unique identifier. Contains car details
 * such as object ID, production year, associated model, and categories. Uses Lombok annotations to
 * reduce boilerplate code for getters, setters, constructors, and toString generation.
 *
 * @author Serhii Bohdan
 * @see AbstractResponseDto
 * @see ModelResponseDto
 * @see CategoryResponseDto
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
public class CarResponseDto extends AbstractResponseDto {

    /**
     * The unique object identifier of the car.
     */
    private String objectId;

    /**
     * The production year of the car.
     */
    private Integer productionYear;

    /**
     * The model associated with the car.
     */
    private ModelResponseDto model;

    /**
     * The set of categories associated with the car.
     */
    private Set<CategoryResponseDto> categories;

}
