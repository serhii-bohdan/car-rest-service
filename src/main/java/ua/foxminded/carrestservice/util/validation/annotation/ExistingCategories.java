package ua.foxminded.carrestservice.util.validation.annotation;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ua.foxminded.carrestservice.util.validation.ExistingCategoriesValidator;

/**
 * Annotation for validating that a collection of category IDs exists in the car rest service system.
 * Applied to fields to ensure all provided category IDs correspond to existing categories.
 * Validated by {@link ExistingCategoriesValidator}. Includes default error message, groups, and
 * payload for validation configuration. Annotated with {@code @Documented}, {@code @Constraint},
 * {@code @Target}, and {@code @Retention} for Java Bean Validation integration.
 *
 * @author Serhii Bohdan
 * @see ExistingCategoriesValidator
 * @see jakarta.validation.Constraint
 * @see jakarta.validation.Payload
 * @see java.lang.annotation.Documented
 * @see java.lang.annotation.Target
 * @see java.lang.annotation.Retention
 */
@Documented
@Constraint(validatedBy = ExistingCategoriesValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistingCategories {

    /**
     * Defines the default error message when validation fails.
     *
     * @return the default error message
     */
    String message() default "No category with ID from the set";

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
