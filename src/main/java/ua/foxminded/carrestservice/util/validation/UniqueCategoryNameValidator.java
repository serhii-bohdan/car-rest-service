package ua.foxminded.carrestservice.util.validation;

import static ua.foxminded.carrestservice.util.validation.ValidationErrorMessages.*;
import java.util.Objects;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.foxminded.carrestservice.repository.CategoryRepository;
import ua.foxminded.carrestservice.util.validation.annotation.UniqueCategoryName;

/**
 * Validator for the {@link UniqueCategoryName} annotation in the car rest service system.
 * Implements {@link ConstraintValidator} to validate that a category name is unique in the database.
 * Uses {@link CategoryRepository} to check for existing category names. Annotated with
 * {@code @Component} for Spring dependency injection, {@code @RequiredArgsConstructor} for
 * constructor injection, and {@code @Slf4j} for logging.
 *
 * @author Serhii Bohdan
 * @see UniqueCategoryName
 * @see CategoryRepository
 * @see jakarta.validation.ConstraintValidator
 * @see lombok.RequiredArgsConstructor
 * @see lombok.extern.slf4j.Slf4j
 * @see org.springframework.stereotype.Component
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UniqueCategoryNameValidator implements ConstraintValidator<UniqueCategoryName, String> {

    /**
     * Repository for accessing and managing {@link ua.foxminded.carrestservice.entity.Category} entities.
     */
    private final CategoryRepository categoryRepository;

    /**
     * The category name being validated.
     */
    private String value;

    /**
     * The validation context for building constraint violations.
     */
    private ConstraintValidatorContext context;

    /**
     * Initializes the validator with the {@link UniqueCategoryName} annotation.
     *
     * @param constraintAnnotation the {@link UniqueCategoryName} annotation instance
     */
    @Override
    public void initialize(UniqueCategoryName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates that the provided category name is unique in the database.
     * Returns {@code false} if the name is null, blank, or already exists, adding appropriate
     * violation messages. Logs validation details.
     *
     * @param value   the category name to validate
     * @param context the validation context for building constraint violations
     * @return {@code true} if the category name is unique, {@code false} otherwise
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        log.debug("Validating category name: {}", value);
        this.value = value;
        this.context = context;

        if (Objects.isNull(this.value) || this.value.isBlank()) {
            log.debug("Category name is null or blank");
            addViolationMessageIfInvalid(true, CATEGORY_NAME_MANDATORY);
            return false;
        }

        boolean isUnique = !categoryRepository.existsByName(this.value);
        log.debug("Category name uniqueness check: isUnique={}", isUnique);
        addViolationMessageIfInvalid(!isUnique, CATEGORY_NAME_NOT_UNIQUE.formatted(this.value));
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
