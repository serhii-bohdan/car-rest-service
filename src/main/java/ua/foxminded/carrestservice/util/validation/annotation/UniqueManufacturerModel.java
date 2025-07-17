package ua.foxminded.carrestservice.util.validation.annotation;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ua.foxminded.carrestservice.util.validation.UniqueManufacturerModelValidator;

/**
 * Annotation for validating the uniqueness of a model name for a manufacturer in the car rest service system.
 * Applied to types to ensure the model name is unique for the specified manufacturer.
 * Validated by {@link UniqueManufacturerModelValidator}. Includes default error message, groups, and
 * payload for validation configuration. Annotated with {@code @Documented}, {@code @Constraint},
 * {@code @Target}, and {@code @Retention} for Java Bean Validation integration.
 *
 * @author Serhii Bohdan
 * @see UniqueManufacturerModelValidator
 * @see jakarta.validation.Constraint
 * @see jakarta.validation.Payload
 * @see java.lang.annotation.Documented
 * @see java.lang.annotation.Target
 * @see java.lang.annotation.Retention
 */
@Documented
@Constraint(validatedBy = UniqueManufacturerModelValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueManufacturerModel {

    /**
     * Defines the default error message when validation fails.
     *
     * @return the default error message
     */
    String message() default "Manufacturer already has such a model";

    /**
     * Specifies the validation groups targeted by this constraint.
     *
     * @return the validation groups
     */
    Class<?>[] groups() default {};

    /**
     * Specifies the payload associated with this constraint for additional metadata.
     *
     * @return the payload classes
     */
    Class<? extends Payload>[] payload() default {};

}
