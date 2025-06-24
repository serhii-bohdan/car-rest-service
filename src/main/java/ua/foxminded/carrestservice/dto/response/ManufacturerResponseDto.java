package ua.foxminded.carrestservice.dto.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object (DTO) for representing a manufacturer in response operations in the car rest
 * service system. Extends {@link AbstractResponseDto} to inherit a unique identifier. Contains the
 * manufacturer's name. Uses Lombok annotations to reduce boilerplate code for getters, setters,
 * constructors, and toString generation.
 *
 * @author Serhii Bohdan
 * @see AbstractResponseDto
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
public class ManufacturerResponseDto extends AbstractResponseDto {

    /**
     * The name of the manufacturer.
     */
    private String name;

}
