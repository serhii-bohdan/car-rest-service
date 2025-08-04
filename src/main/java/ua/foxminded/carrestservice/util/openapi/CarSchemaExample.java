package ua.foxminded.carrestservice.util.openapi;

/**
 * Utility class providing JSON schema examples for OpenAPI documentation of car-related endpoints.
 * Contains static constants with JSON strings representing responses and request bodies for
 * car operations in the car rest service system. These examples are used to document
 * the API in Swagger UI.
 *
 * @author Serhii Bohdan
 */
public class CarSchemaExample {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private CarSchemaExample() {
    }

    /**
     * JSON example of a paginated response containing a list of cars with pagination metadata.
     * Includes car details such as ID, object ID, production year, model, manufacturer, and categories.
     */
    public static final String CARS_PAGE_RESPONSE = """
        {
          "content": [
            {
              "id": 1,
              "objectId": "ZRgPP9dBMm",
              "productionYear": 2020,
              "model": {
                "id": 1,
                "name": "Q3",
                "manufacturer": {
                  "id": 1,
                  "name": "Audi"
                }
              },
              "categories": [
                {
                  "id": 1,
                  "name": "SUV"
                }
              ]
            },
            {
              "id": 2,
              "objectId": "cptB1C1NSL",
              "productionYear": 2020,
              "model": {
                "id": 2,
                "name": "Malibu",
                "manufacturer": {
                  "id": 2,
                  "name": "Chevrolet"
                }
              },
              "categories": [
                {
                  "id": 2,
                  "name": "Sedan"
                }
              ]
            },
            {
              "id": 3,
              "objectId": "ElhqsRZDnP",
              "productionYear": 2020,
              "model": {
                "id": 3,
                "name": "Escalade ESV",
                "manufacturer": {
                  "id": 3,
                  "name": "Cadillac"
                }
              },
              "categories": [
                {
                  "id": 1,
                  "name": "SUV"
                }
              ]
            }
          ],
          "page": {
            "size": 3,
            "number": 0,
            "totalElements": 9837,
            "totalPages": 3279
          }
        }
        """;

    /**
     * JSON example of a response containing details of a single car.
     * Includes car details such as ID, object ID, production year, model, manufacturer, and categories.
     */
    public static final String SINGLE_CAR_RESPONSE = """
        {
          "id": 1,
          "objectId": "ZRgPP9dBMm",
          "productionYear": 2020,
          "model": {
            "id": 1,
            "name": "Q3",
            "manufacturer": {
              "id": 1,
              "name": "Audi"
            }
          },
          "categories": [
            {
              "id": 1,
              "name": "SUV"
            }
          ]
        }
        """;

    /**
     * JSON example of a request body for creating a new car.
     * Includes production year, model ID, and a list of category IDs.
     */
    public static final String CAR_TO_CREATE = """
        {
          "objectId": null,
          "productionYear": 2025,
          "modelId": 8,
          "categoryIds": [
            4, 5, 6
          ]
        }
        """;

    /**
     * JSON example of a response for a newly created car.
     * Includes the assigned ID, object ID, production year, model, and categories.
     */
    public static final String CREATED_CAR_RESPONSE = """
        {
          "id": 9837,
          "objectId": "9270e9318c",
          "productionYear": 2025,
          "model": {
            "id": 8,
            "name": null,
            "manufacturer": null
          },
          "categories": [
            {
              "id": 4,
              "name": "Convertible"
            },
            {
              "id": 6,
              "name": "Van/Minivan"
            },
            {
              "id": 5,
              "name": "Pickup"
            }
          ]
        }
        """;

    /**
     * JSON example of a request body for updating an existing car.
     * Includes the car ID, production year, model ID, and a list of category IDs.
     */
    public static final String CAR_TO_UPDATE = """
        {
          "id": 8,
          "productionYear": 2026,
          "modelId": 10,
          "categoryIds": [
            7, 9
          ]
        }
        """;

    /**
     * JSON example of a response for an updated car.
     * Includes the updated car details such as ID, object ID, production year, model, and categories.
     */
    public static final String UPDATED_CAR_RESPONSE = """
        {
          "id": 8,
          "objectId": "4q7L9FAU2S",
          "productionYear": 2026,
          "model": {
            "id": 10,
            "name": null,
            "manufacturer": null
          },
          "categories": [
            {
              "id": 9,
              "name": "SUV1992"
            },
            {
              "id": 7,
              "name": "Hatchback"
            }
          ]
        }
        """;

}
