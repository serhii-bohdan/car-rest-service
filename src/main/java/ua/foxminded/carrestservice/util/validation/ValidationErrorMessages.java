package ua.foxminded.carrestservice.util.validation;

/**
 * Utility class containing error message constants for validation in the car rest service system.
 * Defines static final strings for validation errors related to manufacturers, categories, models,
 * and production years. Includes a private constructor to prevent instantiation.
 *
 * @author Serhii Bohdan
 */
public class ValidationErrorMessages {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ValidationErrorMessages() {
    }

    /**
     * Error message for incorrect DTO type during validation.
     */
    public static final String INCORRECT_TYPE = "Incorrect type for validation: must be ModelCreateDto or ModelUpdateDto";

    /**
     * Error message for missing object ID.
     */
    public static final String OBJECT_ID_MANDATORY = "Object ID is mandatory";

    /**
     * Error message for missing manufacturer name.
     */
    public static final String MANUFACTURER_NAME_MANDATORY = "Manufacturer's name is mandatory";

    /**
     * Error message for non-unique manufacturer name, formatted with the name.
     */
    public static final String MANUFACTURER_NAME_NOT_UNIQUE = "Manufacturer named %s already exists";

    /**
     * Error message for missing manufacturer ID.
     */
    public static final String MANUFACTURER_ID_MANDATORY = "Manufacturer ID is mandatory";

    /**
     * Error message for non-existent manufacturer, formatted with the ID.
     */
    public static final String MANUFACTURER_NOT_EXIST = "Manufacturer with ID %s does not exist";

    /**
     * Error message for missing category name.
     */
    public static final String CATEGORY_NAME_MANDATORY = "Category name is mandatory";

    /**
     * Error message for non-unique category name, formatted with the name.
     */
    public static final String CATEGORY_NAME_NOT_UNIQUE = "Category named %s already exists";

    /**
     * Error message for missing category IDs in a request.
     */
    public static final String CATEGORY_IDS_REQUIRED = "At least one category ID is required";

    /**
     * Error message for null category ID.
     */
    public static final String CATEGORY_ID_MANDATORY = "Category ID cannot be null";

    /**
     * Error message for non-existent category, formatted with the set of IDs.
     */
    public static final String CATEGORY_NOT_EXIST = "No category with ID from the set %s";

    /**
     * Error message for missing model name.
     */
    public static final String MODEL_NAME_MANDATORY = "Model name is mandatory";

    /**
     * Error message for non-unique model name, formatted with the name.
     */
    public static final String MODEL_NAME_NOT_UNIQUE = "Manufacturer already has a model called %s";

    /**
     * Error message for missing model ID.
     */
    public static final String MODEL_ID_MANDATORY = "Model ID is mandatory";

    /**
     * Error message for non-existent model, formatted with the ID.
     */
    public static final String MODEL_NOT_EXIST = "Model with Id %s does not exist";

    /**
     * Error message for missing production year.
     */
    public static final String PRODUCTION_YEAR_MANDATORY = "Production year is mandatory";

    /**
     * Error message for production year earlier than 1885.
     */
    public static final String PRODUCTION_YEAR_LATER = "Production year must be 1885 or later";

}
