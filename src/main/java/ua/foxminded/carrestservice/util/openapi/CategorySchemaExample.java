package ua.foxminded.carrestservice.util.openapi;

/**
 * Utility class providing JSON schema examples for OpenAPI documentation of category-related endpoints.
 * Contains static constants with JSON strings representing responses and request bodies for
 * category operations in the car rest service system. These examples are used to document
 * the API in Swagger UI.
 *
 * @author Serhii Bohdan
 */
public class CategorySchemaExample {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private CategorySchemaExample() {
    }

    /**
     * JSON example of a paginated response containing a list of categories with pagination metadata.
     * Includes category details such as ID and name.
     */
    public static final String CATEGORIES_PAGE_RESPONSE = """
        {
          "content": [
            {
              "id": 1,
              "name": "SUV"
            },
            {
              "id": 2,
              "name": "Sedan"
            },
            {
              "id": 3,
              "name": "Coupe"
            }
          ],
          "page": {
            "size": 3,
            "number": 0,
            "totalElements": 10,
            "totalPages": 4
          }
        }
        """;

    /**
     * JSON example of a response containing details of a single category.
     * Includes category details such as ID and name.
     */
    public static final String SINGLE_CATEGORY_RESPONSE = """
        {
          "id": 1,
          "name": "SUV"
        }
        """;

    /**
     * JSON example of a request body for creating a new category.
     * Includes the category name.
     */
    public static final String CATEGORY_TO_CREATE = """
        {
          "name": "New Category"
        }
        """;

    /**
     * JSON example of a response for a newly created category.
     * Includes the assigned ID and category name.
     */
    public static final String CREATED_CATEGORY_RESPONSE = """
        {
          "id": 11,
          "name": "New Category"
        }
        """;

    /**
     * JSON example of a request body for updating an existing category.
     * Includes the category ID and updated name.
     */
    public static final String CATEGORY_TO_UPDATE = """
        {
          "id": 1,
          "name": "Updated Category Name"
        }
        """;

    /**
     * JSON example of a response for an updated category.
     * Includes the updated category details such as ID and name.
     */
    public static final String UPDATED_CATEGORY_RESPONSE = """
        {
          "id": 1,
          "name": "Updated Category Name"
        }
        """;

}
