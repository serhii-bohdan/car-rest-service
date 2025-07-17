package ua.foxminded.carrestservice.util.validation.annotation;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ua.foxminded.carrestservice.util.validation.UniqueCategoryNameValidator;

/**
 * Annotation for validating the uniqueness of a category name in the car rest service system.
 * Applied to fields to ensure the provided category name does not already exist.
 * Validated by {@link UniqueCategoryNameValidator}. Includes default error message, groups, and
 * payload for validation configuration. Annotated with {@code @Documented}, {@code @Constraint},
 * {@code @Target}, and {@code @Retention} for Java Bean Validation integration.
 *
 * @author Serhii Bohdan
 * @see UniqueCategoryNameValidator
 * @see jakarta.validation.Constraint
 * @see jakarta.validation.Payload
 * @see java.lang.annotation.Documented
 * @see java.lang.annotation.Target
 * @see java.lang.annotation.Retention
 */
@Documented
@Constraint(validatedBy = UniqueCategoryNameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueCategoryName {

    /**
     * Defines the default error message when validation fails.
     *
     * @return the default error message
     */
    String message() default "Category with this name already exists";

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
