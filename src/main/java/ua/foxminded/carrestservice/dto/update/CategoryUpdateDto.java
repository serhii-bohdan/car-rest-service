package ua.foxminded.carrestservice.dto.update;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ua.foxminded.carrestservice.util.validation.annotation.UniqueCategoryName;

/**
 * Data Transfer Object (DTO) for updating a category in the car rest service system.
 * Extends {@link AbstractUpdateDto} to inherit a unique identifier. Contains the category's
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
public class CategoryUpdateDto extends AbstractUpdateDto {

    /**
     * The name of the category to be updated.
     */
    @UniqueCategoryName
    private String name;

}
