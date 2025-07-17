package ua.foxminded.carrestservice.util.specification;

import org.springframework.data.jpa.domain.Specification;
import ua.foxminded.carrestservice.entity.Car;
import ua.foxminded.carrestservice.entity.Category;
import ua.foxminded.carrestservice.entity.Manufacturer;
import ua.foxminded.carrestservice.entity.Model;

/**
 * Utility class for creating JPA {@link Specification} objects to filter {@link Car} entities.
 * Provides static methods to build specifications for querying cars based on manufacturer, model,
 * category, and production year criteria. Uses constant attribute names for consistent access to
 * entity fields and relationships.
 *
 * @author Serhii Bohdan
 * @see org.springframework.data.jpa.domain.Specification
 * @see ua.foxminded.carrestservice.entity.Car
 */
public class CarSpecification {

    /**
     * Attribute name for the production year field in the {@link Car} entity.
     */
    private static final String PRODUCTION_YEAR_ATTRIBUTE = "productionYear";

    /**
     * Attribute name for the model relationship in the {@link Car} entity.
     */
    private static final String MODEL_ATTRIBUTE = "model";

    /**
     * Attribute name for the manufacturer relationship in the {@link Model} entity.
     */
    private static final String MANUFACTURER_ATTRIBUTE = "manufacturer";

    /**
     * Attribute name for the categories relationship in the {@link Car} entity.
     */
    private static final String CATEGORIES_ATTRIBUTE = "categories";

    /**
     * Attribute name for the name field in the {@link Manufacturer} entity.
     */
    private static final String MANUFACTURER_NAME_ATTRIBUTE = "name";

    /**
     * Attribute name for the name field in the {@link Model} entity.
     */
    private static final String MODEL_NAME_ATTRIBUTE = "name";

    /**
     * Attribute name for the name field in the {@link Category} entity.
     */
    private static final String CATEGORY_NAME_ATTRIBUTE = "name";

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private CarSpecification() {
    }

    /**
     * Creates a specification to filter cars by manufacturer name.
     *
     * @param manufacturerName the name of the manufacturer to filter by
     * @return a {@link Specification} for filtering {@link Car} entities by manufacturer name
     */
    public static Specification<Car> withManufacturer(String manufacturerName) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.join(MODEL_ATTRIBUTE).join(MANUFACTURER_ATTRIBUTE).get(MANUFACTURER_NAME_ATTRIBUTE), manufacturerName);
    }

    /**
     * Creates a specification to filter cars by model name.
     *
     * @param modelName the name of the model to filter by
     * @return a {@link Specification} for filtering {@link Car} entities by model name
     */
    public static Specification<Car> withModel(String modelName) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.join(MODEL_ATTRIBUTE).get(MODEL_NAME_ATTRIBUTE), modelName);
    }

    /**
     * Creates a specification to filter cars by category name.
     *
     * @param categoryName the name of the category to filter by
     * @return a {@link Specification} for filtering {@link Car} entities by category name
     */
    public static Specification<Car> withCategory(String categoryName) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.join(CATEGORIES_ATTRIBUTE).get(CATEGORY_NAME_ATTRIBUTE), categoryName);
    }

    /**
     * Creates a specification to filter cars with a production year greater than or equal to the specified year.
     *
     * @param minYear the minimum production year to filter by
     * @return a {@link Specification} for filtering {@link Car} entities by minimum production year
     */
    public static Specification<Car> withProductionYearGreaterThanOrEqual(Integer minYear) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.greaterThanOrEqualTo(root.get(PRODUCTION_YEAR_ATTRIBUTE), minYear);
    }

    /**
     * Creates a specification to filter cars with a production year less than or equal to the specified year.
     *
     * @param maxYear the maximum production year to filter by
     * @return a {@link Specification} for filtering {@link Car} entities by maximum production year
     */
    public static Specification<Car> withProductionYearLessThanOrEqual(Integer maxYear) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.lessThanOrEqualTo(root.get(PRODUCTION_YEAR_ATTRIBUTE), maxYear);
    }

    /**
     * Creates a specification to filter cars with a production year within the specified range.
     *
     * @param minYear the minimum production year to filter by
     * @param maxYear the maximum production year to filter by
     * @return a {@link Specification} for filtering {@link Car} entities by production year range
     */
    public static Specification<Car> withProductionYearBetween(Integer minYear, Integer maxYear) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.between(root.get(PRODUCTION_YEAR_ATTRIBUTE), minYear, maxYear);
    }

}
