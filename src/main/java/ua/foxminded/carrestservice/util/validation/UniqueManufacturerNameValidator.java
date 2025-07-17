package ua.foxminded.carrestservice.util.validation;

import static ua.foxminded.carrestservice.util.validation.ValidationErrorMessages.MANUFACTURER_NAME_MANDATORY;
import static ua.foxminded.carrestservice.util.validation.ValidationErrorMessages.MANUFACTURER_NAME_NOT_UNIQUE;
import java.util.Objects;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.foxminded.carrestservice.repository.ManufacturerRepository;
import ua.foxminded.carrestservice.util.validation.annotation.UniqueManufacturerName;

/**
 * Validator for the {@link UniqueManufacturerName} annotation in the car rest service system.
 * Implements {@link ConstraintValidator} to validate that a manufacturer name is unique in the
 * database. Uses {@link ManufacturerRepository} to check for existing manufacturer names.
 * Annotated with {@code @Component} for Spring dependency injection,
 * {@code @RequiredArgsConstructor} for constructor injection, and {@code @Slf4j} for logging.
 *
 * @author Serhii Bohdan
 * @see UniqueManufacturerName
 * @see ManufacturerRepository
 * @see jakarta.validation.ConstraintValidator
 * @see lombok.RequiredArgsConstructor
 * @see lombok.extern.slf4j.Slf4j
 * @see org.springframework.stereotype.Component
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UniqueManufacturerNameValidator implements ConstraintValidator<UniqueManufacturerName, String> {

    /**
     * Repository for accessing and managing {@link ua.foxminded.carrestservice.entity.Manufacturer} entities.
     */
    private final ManufacturerRepository manufacturerRepository;

    /**
     * The manufacturer name being validated.
     */
    private String value;

    /**
     * The validation context for building constraint violations.
     */
    private ConstraintValidatorContext context;

    /**
     * Initializes the validator with the {@link UniqueManufacturerName} annotation.
     *
     * @param constraintAnnotation the {@link UniqueManufacturerName} annotation instance
     */
    @Override
    public void initialize(UniqueManufacturerName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates that the provided manufacturer name is unique in the database.
     * Returns {@code false} if the name is null, blank, or already exists, adding appropriate
     * violation messages. Logs validation details.
     *
     * @param value   the manufacturer name to validate
     * @param context the validation context for building constraint violations
     * @return {@code true} if the manufacturer name is unique, {@code false} otherwise
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        log.debug("Validating manufacturer name: {}", value);
        this.value = value;
        this.context = context;

        if (Objects.isNull(this.value) || this.value.isBlank()) {
            log.debug("Manufacturer name is null or blank");
            addViolationMessageIfInvalid(true, MANUFACTURER_NAME_MANDATORY);
            return false;
        }

        boolean isUnique = !manufacturerRepository.existsByName(value);
        log.debug("Manufacturer name uniqueness check: isUnique={}", isUnique);
        addViolationMessageIfInvalid(!isUnique, MANUFACTURER_NAME_NOT_UNIQUE.formatted(this.value));
        return isUnique;
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
