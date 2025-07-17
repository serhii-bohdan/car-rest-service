package ua.foxminded.carrestservice.util.validation;

import static ua.foxminded.carrestservice.util.validation.ValidationErrorMessages.MODEL_ID_MANDATORY;
import static ua.foxminded.carrestservice.util.validation.ValidationErrorMessages.MODEL_NOT_EXIST;
import java.util.Objects;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.foxminded.carrestservice.entity.Model;
import ua.foxminded.carrestservice.repository.ModelRepository;
import ua.foxminded.carrestservice.util.validation.annotation.ExistingModel;

/**
 * Validator for the {@link ExistingModel} annotation in the car rest service system.
 * Implements {@link ConstraintValidator} to validate that a model ID corresponds to an existing
 * model in the database. Uses {@link ModelRepository} to check model existence. Annotated with
 * {@code @Component} for Spring dependency injection, {@code @RequiredArgsConstructor} for
 * constructor injection, and {@code @Slf4j} for logging.
 *
 * @author Serhii Bohdan
 * @see ExistingModel
 * @see ModelRepository
 * @see jakarta.validation.ConstraintValidator
 * @see lombok.RequiredArgsConstructor
 * @see lombok.extern.slf4j.Slf4j
 * @see org.springframework.stereotype.Component
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExistingModelValidator implements ConstraintValidator<ExistingModel, Long> {

    /**
     * Repository for accessing and managing {@link Model} entities.
     */
    private final ModelRepository modelRepository;

    /**
     * The model ID being validated.
     */
    private Long value;

    /**
     * The validation context for building constraint violations.
     */
    private ConstraintValidatorContext context;

    /**
     * Initializes the validator with the {@link ExistingModel} annotation.
     *
     * @param constraintAnnotation the {@link ExistingModel} annotation instance
     */
    @Override
    public void initialize(ExistingModel constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates that the provided model ID exists in the database.
     * Returns {@code false} if the ID is null or does not correspond to an existing model,
     * adding appropriate violation messages. Logs validation details.
     *
     * @param value   the model ID to validate
     * @param context the validation context for building constraint violations
     * @return {@code true} if the model ID exists, {@code false} otherwise
     */
    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        log.debug("Validating model ID: {}", value);
        this.value = value;
        this.context = context;

        if (Objects.isNull(this.value)) {
            log.debug("Model ID is null");
            addViolationMessageIfInvalid(true, MODEL_ID_MANDATORY);
            return false;
        }

        boolean isValid = modelRepository.existsById(this.value);
        log.debug("Model ID existence check: isValid={}", isValid);

        addViolationMessageIfInvalid(!isValid, MODEL_NOT_EXIST.formatted(this.value));
        return isValid;
    }

    private void addViolationMessageIfInvalid(boolean isInvalid, String message) {
        if (isInvalid) {
            log.debug("Adding violation message: {}", message);
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
        }
    }

}
