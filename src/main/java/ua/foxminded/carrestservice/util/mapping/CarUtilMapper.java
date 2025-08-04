package ua.foxminded.carrestservice.util.mapping;

import java.time.Year;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.foxminded.carrestservice.entity.Car;
import ua.foxminded.carrestservice.entity.Category;
import ua.foxminded.carrestservice.entity.Model;
import ua.foxminded.carrestservice.exception.EntityNotFoundException;
import ua.foxminded.carrestservice.repository.CarRepository;
import ua.foxminded.carrestservice.repository.CategoryRepository;
import ua.foxminded.carrestservice.util.mapping.annotation.CarCategoriesMapping;
import ua.foxminded.carrestservice.util.mapping.annotation.CarModelUpdateMapping;
import ua.foxminded.carrestservice.util.mapping.annotation.CarObjectIdMapping;
import ua.foxminded.carrestservice.util.mapping.annotation.CarProductionYearMapping;

/**
 * Utility class for custom mappings in the car rest service system, used by MapStruct mappers.
 * Provides methods for generating unique object IDs, converting production years, mapping model IDs
 * to {@link Model} entities, and converting category IDs to {@link Category} sets. Annotated with
 * {@code @Component} for Spring integration, {@code @Slf4j} for logging, and
 * {@code @RequiredArgsConstructor} for dependency injection of repositories.
 *
 * @author Serhii Bohdan
 * @see CarRepository
 * @see CategoryRepository
 * @see ua.foxminded.carrestservice.mapper.CarMapper
 * @see lombok.RequiredArgsConstructor
 * @see lombok.extern.slf4j.Slf4j
 * @see org.springframework.stereotype.Component
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CarUtilMapper {

    /**
     * Length of the generated object ID.
     */
    private static final int OBJECT_ID_LENGTH = 10;

    /**
     * Hyphen character used in UUID generation.
     */
    private static final String HYPHEN = "-";

    /**
     * Empty string used for replacing hyphens in UUID.
     */
    private static final String EMPTY_STRING = "";

    /**
     * Error message template for category not found exceptions.
     */
    private static final String CATEGORY_NOT_FOUND_MESSAGE = "Category with ID %s not found.";

    /**
     * Repository for accessing {@link Car} entities.
     */
    private final CarRepository carRepository;

    /**
     * Repository for accessing {@link Category} entities.
     */
    private final CategoryRepository categoryRepository;

    /**
     * Generates a unique object ID for a {@link Car} entity.
     * Uses {@link UUID} to create a random ID of fixed length ({@code OBJECT_ID_LENGTH}). Ensures
     * uniqueness by checking against existing IDs in {@link CarRepository}. Annotated with
     * {@link CarObjectIdMapping} for MapStruct integration.
     *
     * @param objectId the input object ID (ignored in generation)
     * @return a unique object ID string
     */
    @CarObjectIdMapping
    public String generateObjectId(String objectId) {
        log.info("Generating new objectId for input: {}", objectId);
        String generatedObjectId = generateRandomUuidWithFixedLength();
        boolean isObjectIdDuplicated = carRepository.findAll().stream()
            .map(Car::getObjectId)
            .anyMatch(generatedObjectId::equals);

        if (isObjectIdDuplicated) {
            log.warn("Generated objectId {} is duplicated, retrying", generatedObjectId);
            return generateObjectId(objectId);
        }

        log.debug("Generated unique objectId: {}", generatedObjectId);
        return generatedObjectId;
    }

    /**
     * Generates a random UUID with a fixed length.
     * Removes hyphens from a {@link UUID} and truncates or retries to match {@code OBJECT_ID_LENGTH}.
     *
     * @return a truncated UUID string of fixed length
     */
    private String generateRandomUuidWithFixedLength() {
        String uuid = UUID.randomUUID().toString().replace(HYPHEN, EMPTY_STRING);
        log.debug("Generated UUID: {}", uuid);

        if (uuid.length() >= OBJECT_ID_LENGTH) {
            String truncatedUuid = uuid.substring(0, OBJECT_ID_LENGTH);
            log.debug("Truncated UUID to {}: {}", OBJECT_ID_LENGTH, truncatedUuid);
            return truncatedUuid;
        }

        log.warn("Generated UUID length {} is less than {}, retrying", uuid.length(), OBJECT_ID_LENGTH);
        return generateRandomUuidWithFixedLength();
    }

    /**
     * Converts an integer production year to a {@link Year} object.
     * Returns null if the input is null. Annotated with {@link CarProductionYearMapping} for
     * MapStruct integration.
     *
     * @param productionYear the production year as an integer
     * @return the converted {@link Year} object or null
     */
    @CarProductionYearMapping
    public Year convertToYear(Integer productionYear) {
        log.debug("Converting productionYear: {}", productionYear);
        if (productionYear == null) {
            log.warn("productionYear is null, returning null");
            return null;
        }

        Year year = Year.of(productionYear);
        log.debug("Converted to Year: {}", year);
        return year;
    }

    /**
     * Converts a model ID to a {@link Model} entity.
     * Creates a {@link Model} with the specified ID. Annotated with {@link CarModelUpdateMapping}
     * for MapStruct integration.
     *
     * @param modelId the ID of the model
     * @return the {@link Model} entity with the specified ID
     */
    @CarModelUpdateMapping
    public Model convertToModel(Long modelId) {
        log.info("Converting modelId to Model: {}", modelId);
        Model model = Model.builder()
            .id(modelId)
            .build();

        log.debug("Converted to Model: {}", model);
        return model;
    }

    /**
     * Converts a set of category IDs to a set of {@link Category} entities.
     * Retrieves categories from {@link CategoryRepository} by IDs, throwing
     * {@link EntityNotFoundException} if any ID is not found. Returns an empty set if input is null
     * or empty. Annotated with {@link CarCategoriesMapping} for MapStruct integration.
     *
     * @param categoryIds the set of category IDs
     * @return a set of {@link Category} entities
     * @throws EntityNotFoundException if a category ID is not found
     */
    @CarCategoriesMapping
    public Set<Category> convertToCategoriesSet(Set<Long> categoryIds) {
        log.info("Converting categoryIds to categories: {}", categoryIds);
        if (categoryIds == null || categoryIds.isEmpty()) {
            log.debug("categoryIds is null or empty, returning empty set");
            return new HashSet<>();
        }

        return categoryIds.stream()
            .map(id -> categoryRepository.findById(id).orElseThrow(() -> {
                log.error("Category with ID {} not found", id);
                return new EntityNotFoundException(CATEGORY_NOT_FOUND_MESSAGE.formatted(id));
            }))
            .collect(Collectors.toSet());
    }

}
