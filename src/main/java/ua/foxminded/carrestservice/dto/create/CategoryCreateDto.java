package ua.foxminded.carrestservice.dto.create;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object (DTO) for creating a new category in the car rest service system.
 * Extends {@link AbstractCreateDto} to inherit common DTO structure. Contains the category's
 * name for creation operations. Uses Lombok annotations to reduce boilerplate code for getters,
 * setters, constructors, and toString generation.
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
public class CategoryCreateDto extends AbstractCreateDto {

    /**
     * The name of the category to be created.
     */
    private String name;

}
