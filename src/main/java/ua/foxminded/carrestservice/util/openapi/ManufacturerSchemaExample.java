package ua.foxminded.carrestservice.util.openapi;

/**
 * Utility class providing JSON schema examples for OpenAPI documentation of manufacturer-related endpoints.
 * Contains static constants with JSON strings representing responses and request bodies for
 * manufacturer operations in the car rest service system. These examples are used to document
 * the API in Swagger UI.
 *
 * @author Serhii Bohdan
 */
public class ManufacturerSchemaExample {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ManufacturerSchemaExample() {
    }

    /**
     * JSON example of a paginated response containing a list of manufacturers with pagination metadata.
     * Includes manufacturer details such as ID and name.
     */
    public static final String MANUFACTURERS_PAGE_RESPONSE = """
        {
          "content": [
            {
              "id": 1,
              "name": "Audi"
            },
            {
              "id": 2,
              "name": "Chevrolet"
            },
            {
              "id": 3,
              "name": "Cadillac"
            }
          ],
          "page": {
            "size": 3,
            "number": 0,
            "totalElements": 64,
            "totalPages": 22
          }
        }
        """;

    /**
     * JSON example of a response containing details of a single manufacturer.
     * Includes manufacturer details such as ID and name.
     */
    public static final String SINGLE_MANUFACTURER_RESPONSE = """
        {
          "id": 1,
          "name": "Audi"
        }
        """;

    /**
     * JSON example of a request body for creating a new manufacturer.
     * Includes the manufacturer name.
     */
    public static final String MANUFACTURER_TO_CREATE = """
        {
          "name": "New Manufacturer"
        }
        """;

    /**
     * JSON example of a response for a newly created manufacturer.
     * Includes the assigned ID and manufacturer name.
     */
    public static final String CREATED_MANUFACTURER_RESPONSE = """
        {
          "id": 65,
          "name": "New Manufacturer"
        }
        """;

    /**
     * JSON example of a request body for updating an existing manufacturer.
     * Includes the manufacturer ID and updated name.
     */
    public static final String MANUFACTURER_TO_UPDATE = """
        {
          "id": 1,
          "name": "Updated Manufacturer Name"
        }
        """;

    /**
     * JSON example of a response for an updated manufacturer.
     * Includes the updated manufacturer details such as ID and name.
     */
    public static final String UPDATED_MANUFACTURER_RESPONSE = """
        {
          "id": 1,
          "name": "Updated Manufacturer Name"
        }
        """;

}
