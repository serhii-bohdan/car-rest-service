package ua.foxminded.carrestservice.util.validation;

import static ua.foxminded.carrestservice.util.validation.ValidationErrorMessages.*;
import java.util.Objects;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.foxminded.carrestservice.dto.create.ModelCreateDto;
import ua.foxminded.carrestservice.dto.update.ModelUpdateDto;
import ua.foxminded.carrestservice.entity.Manufacturer;
import ua.foxminded.carrestservice.entity.Model;
import ua.foxminded.carrestservice.repository.ManufacturerRepository;
import ua.foxminded.carrestservice.repository.ModelRepository;
import ua.foxminded.carrestservice.util.validation.annotation.UniqueManufacturerModel;

/**
 * Validator for the {@link UniqueManufacturerModel} annotation in the car rest service system.
 * Implements {@link ConstraintValidator} to validate that a model name is unique for a given
 * manufacturer in the database. Supports validation for {@link ModelCreateDto} and
 * {@link ModelUpdateDto}. Uses {@link ManufacturerRepository} and {@link ModelRepository} to check
 * existence and uniqueness. Annotated with {@code @Component} for Spring dependency injection,
 * {@code @RequiredArgsConstructor} for constructor injection, and {@code @Slf4j} for logging.
 *
 * @author Serhii Bohdan
 * @see UniqueManufacturerModel
 * @see ManufacturerRepository
 * @see ModelRepository
 * @see ModelCreateDto
 * @see ModelUpdateDto
 * @see jakarta.validation.ConstraintValidator
 * @see lombok.RequiredArgsConstructor
 * @see lombok.extern.slf4j.Slf4j
 * @see org.springframework.stereotype.Component
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UniqueManufacturerModelValidator implements ConstraintValidator<UniqueManufacturerModel, Object> {

    /**
     * Repository for accessing and managing {@link Manufacturer} entities.
     */
    private final ManufacturerRepository manufacturerRepository;

    /**
     * Repository for accessing and managing {@link Model} entities.
     */
    private final ModelRepository modelRepository;

    /**
     * The validation context for building constraint violations.
     */
    private ConstraintValidatorContext context;

    /**
     * Initializes the validator with the {@link UniqueManufacturerModel} annotation.
     *
     * @param constraintAnnotation the {@link UniqueManufacturerModel} annotation instance
     */
    @Override
    public void initialize(UniqueManufacturerModel constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates that the model name in the provided DTO is unique for the specified manufacturer.
     * Supports {@link ModelCreateDto} and {@link ModelUpdateDto}. Returns {@code false} if the DTO
     * is invalid, the manufacturer ID is missing or non-existent, or the model name is not unique,
     * adding appropriate violation messages. Logs validation details.
     *
     * @param value   the DTO object to validate (either {@link ModelCreateDto} or {@link ModelUpdateDto})
     * @param context the validation context for building constraint violations
     * @return {@code true} if the model name is unique for the manufacturer, {@code false} otherwise
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        log.debug("Validating model DTO: {}", value);
        this.context = context;
        return switch (value) {
            case ModelCreateDto modelCreateDto -> validateCreateDto(modelCreateDto);
            case ModelUpdateDto modelUpdateDto -> validateUpdateDto(modelUpdateDto);
            default -> {
                log.debug("Invalid DTO type: {}", value.getClass().getSimpleName());
                addViolationMessageIfInvalid(true, INCORRECT_TYPE);
                yield false;
            }
        };
    }

    private boolean validateCreateDto(ModelCreateDto createDto) {
        String modelName = createDto.getName();
        Long manufacturerId = createDto.getManufacturerId();
        log.debug("Validating ModelCreateDto: name={}, manufacturerId={}", modelName, manufacturerId);

        if (isManufacturerInvalid(manufacturerId)) {
            return false;
        } else if (Objects.isNull(modelName) || modelName.isBlank()) {
            log.debug("Model name is null or blank");
            addViolationMessageIfInvalid(true, MODEL_NAME_MANDATORY);
            return false;
        }

        boolean isNameUnique = !modelRepository.existsByManufacturerIdAndName(createDto.getManufacturerId(),
            createDto.getName());
        log.debug("Model name uniqueness check: isUnique={}", isNameUnique);

        addViolationMessageIfInvalid(!isNameUnique, MODEL_NAME_NOT_UNIQUE.formatted(createDto.getName()));
        return isNameUnique;
    }

    private boolean validateUpdateDto(ModelUpdateDto updateDto) {
        String modelName = updateDto.getName();
        Long manufacturerId = updateDto.getManufacturerId();
        log.debug("Validating ModelUpdateDto: id={}, name={}, manufacturerId={}",
            updateDto.getId(), modelName, manufacturerId);

        if (isManufacturerInvalid(manufacturerId)) {
            return false;
        } else if (Objects.isNull(modelName) || modelName.isBlank()) {
            log.debug("Model name is null or blank");
            addViolationMessageIfInvalid(true, MODEL_NAME_MANDATORY);
            return false;
        }

        boolean isNameUnique = !modelRepository.existsByManufacturerIdAndNameAndIdIsNot(updateDto.getManufacturerId(),
            updateDto.getName(), updateDto.getId());
        log.debug("Model name uniqueness check: isUnique={}", isNameUnique);

        addViolationMessageIfInvalid(!isNameUnique, MODEL_NAME_NOT_UNIQUE.formatted(updateDto.getName()));
        return isNameUnique;
    }

    private boolean isManufacturerInvalid(Long manufacturerId) {
        log.debug("Validating manufacturer ID: {}", manufacturerId);
        if (Objects.isNull(manufacturerId)) {
            log.debug("Manufacturer ID is null");
            addViolationMessageIfInvalid(true, MANUFACTURER_ID_MANDATORY);
            return true;
        }

        boolean isInvalid = !manufacturerRepository.existsById(manufacturerId);
        log.debug("Manufacturer existence check: isValid={}", !isInvalid);
        addViolationMessageIfInvalid(isInvalid, MANUFACTURER_NOT_EXIST.formatted(manufacturerId));
        return isInvalid;
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
