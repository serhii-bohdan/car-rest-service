package ua.foxminded.carrestservice.dto.update;

import static ua.foxminded.carrestservice.util.validation.ValidationErrorMessages.*;
import java.util.Set;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ua.foxminded.carrestservice.util.validation.annotation.ExistingCategories;
import ua.foxminded.carrestservice.util.validation.annotation.ExistingModel;

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
    @NotNull(message = PRODUCTION_YEAR_MANDATORY)
    @Min(value = 1885, message = PRODUCTION_YEAR_LATER)
    private Integer productionYear;

    /**
     * The ID of the car model associated with the car.
     */
    @ExistingModel
    private Long modelId;

    /**
     * The set of category IDs associated with the car.
     */
    @Valid
    @ExistingCategories
    private Set<@NotNull(message = CATEGORY_ID_MANDATORY) Long> categoryIds;

}
