package ua.foxminded.carrestservice.util.validation;

import static ua.foxminded.carrestservice.util.validation.ValidationErrorMessages.*;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.foxminded.carrestservice.entity.Category;
import ua.foxminded.carrestservice.repository.CategoryRepository;
import ua.foxminded.carrestservice.util.validation.annotation.ExistingCategories;

/**
 * Validator for the {@link ExistingCategories} annotation in the car rest service system.
 * Implements {@link ConstraintValidator} to validate that a set of category IDs corresponds to
 * existing categories in the database. Uses {@link CategoryRepository} to check category existence.
 * Annotated with {@code @Component} for Spring dependency injection, {@code @RequiredArgsConstructor}
 * for constructor injection, and {@code @Slf4j} for logging.
 *
 * @author Serhii Bohdan
 * @see ExistingCategories
 * @see CategoryRepository
 * @see jakarta.validation.ConstraintValidator
 * @see lombok.RequiredArgsConstructor
 * @see lombok.extern.slf4j.Slf4j
 * @see org.springframework.stereotype.Component
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExistingCategoriesValidator implements ConstraintValidator<ExistingCategories, Set<Long>> {

    /**
     * Repository for accessing and managing {@link Category} entities.
     */
    private final CategoryRepository categoryRepository;

    /**
     * The set of category IDs being validated.
     */
    private Set<Long> value;

    /**
     * The validation context for building constraint violations.
     */
    private ConstraintValidatorContext context;

    /**
     * Initializes the validator with the {@link ExistingCategories} annotation.
     *
     * @param constraintAnnotation the {@link ExistingCategories} annotation instance
     */
    @Override
    public void initialize(ExistingCategories constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates that all provided category IDs in the set exist in the database.
     * Returns {@code false} if the set is null, empty, or contains non-existing category IDs,
     * adding appropriate violation messages. Logs validation details.
     *
     * @param value   the set of category IDs to validate
     * @param context the validation context for building constraint violations
     * @return {@code true} if all category IDs exist, {@code false} otherwise
     */
    @Override
    public boolean isValid(Set<Long> value, ConstraintValidatorContext context) {
        log.debug("Validating category IDs: {}", value);
        this.value = value;
        this.context = context;

        if (Objects.isNull(this.value) || this.value.isEmpty()) {
            log.debug("Category IDs are null or empty");
            addViolationMessageIfInvalid(true, CATEGORY_IDS_REQUIRED);
            return false;
        }

        boolean isCategoryIdsValid = categoryRepository.findAll().stream()
            .map(Category::getId)
            .collect(Collectors.toSet())
            .containsAll(value);
        log.debug("Category IDs validation check: isValid={}", isCategoryIdsValid);

        addViolationMessageIfInvalid(!isCategoryIdsValid, CATEGORY_NOT_EXIST.formatted(this.value));
        return isCategoryIdsValid;
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
