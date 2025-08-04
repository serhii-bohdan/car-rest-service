package ua.foxminded.carrestservice.util.openapi;

/**
 * Utility class providing JSON schema examples for OpenAPI documentation of model-related endpoints.
 * Contains static constants with JSON strings representing responses and request bodies for
 * model operations in the car rest service system. These examples are used to document
 * the API in Swagger UI.
 *
 * @author Serhii Bohdan
 */
public class ModelSchemaExample {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ModelSchemaExample() {
    }

    /**
     * JSON example of a paginated response containing a list of car models with pagination metadata.
     * Includes model details such as ID, name, and associated manufacturer.
     */
    public static final String MODELS_PAGE_RESPONSE = """
        {
          "content": [
            {
              "id": 1,
              "name": "Q3",
              "manufacturer": {
                "id": 1,
                "name": "Updated Manufacturer Name"
              }
            },
            {
              "id": 2,
              "name": "Malibu",
              "manufacturer": {
                "id": 2,
                "name": "Chevrolet"
              }
            },
            {
              "id": 3,
              "name": "Escalade ESV",
              "manufacturer": {
                "id": 3,
                "name": "Cadillac"
              }
            }
          ],
          "page": {
            "size": 3,
            "number": 0,
            "totalElements": 1265,
            "totalPages": 422
          }
        }
        """;

    /**
     * JSON example of a response containing details of a single car model.
     * Includes model details such as ID, name, and associated manufacturer.
     */
    public static final String SINGLE_MODEL_RESPONSE = """
        {
          "id": 1,
          "name": "Q3",
          "manufacturer": {
            "id": 1,
            "name": "Audi"
          }
        }
        """;

    /**
     * JSON example of a request body for creating a new car model.
     * Includes the model name and the ID of the associated manufacturer.
     */
    public static final String MODEL_TO_CREATE = """
        {
          "name": "New Model",
          "manufacturerId": 2
        }
        """;

    /**
     * JSON example of a response for a newly created car model.
     * Includes the assigned ID, model name, and associated manufacturer.
     */
    public static final String CREATED_MODEL_RESPONSE = """
        {
          "id": 1266,
          "name": "New Model",
          "manufacturer": {
            "id": 2,
            "name": null
          }
        }
        """;

    /**
     * JSON example of a request body for updating an existing car model.
     * Includes the model ID, updated name, and the ID of the associated manufacturer.
     */
    public static final String MODEL_TO_UPDATE = """
        {
          "id": 5,
          "name": "Updated Model Name",
          "manufacturerId": 5
        }
        """;

    /**
     * JSON example of a response for an updated car model.
     * Includes the updated model details such as ID, name, and associated manufacturer.
     */
    public static final String UPDATED_MODEL_RESPONSE = """
        {
          "id": 5,
          "name": "Updated Model Name",
          "manufacturer": {
            "id": 5,
            "name": null
          }
        }
        """;

}
