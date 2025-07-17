package ua.foxminded.carrestservice.util.validation.annotation;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ua.foxminded.carrestservice.util.validation.ExistingModelValidator;

/**
 * Annotation for validating that a model ID exists in the car rest service system.
 * Applied to fields to ensure the provided model ID corresponds to an existing model.
 * Validated by {@link ExistingModelValidator}. Includes default error message, groups, and
 * payload for validation configuration. Annotated with {@code @Documented}, {@code @Constraint},
 * {@code @Target}, and {@code @Retention} for Java Bean Validation integration.
 *
 * @author Serhii Bohdan
 * @see ExistingModelValidator
 * @see jakarta.validation.Constraint
 * @see jakarta.validation.Payload
 * @see java.lang.annotation.Documented
 * @see java.lang.annotation.Target
 * @see java.lang.annotation.Retention
 */
@Documented
@Constraint(validatedBy = ExistingModelValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistingModel {

    /**
     * Defines the default error message when validation fails.
     *
     * @return the default error message
     */
    String message() default "No model with this ID exists";

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
