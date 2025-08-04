package ua.foxminded.carrestservice.util.openapi;

/**
 * Utility class providing JSON schema examples for OpenAPI documentation of error responses.
 * Contains static constants with JSON strings representing error responses for various HTTP
 * status codes in the car rest service system. These examples are used to document error
 * responses in Swagger UI.
 *
 * @author Serhii Bohdan
 */
public class ErrorSchemaExample {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ErrorSchemaExample() {
    }

    /**
     * JSON example of an error response for HTTP 404 (Not Found).
     * Includes a list of error messages, the reason phrase, and the status code.
     */
    public static final String NOT_FOUND_RESPONSE = """
        {
          "errors": [
            "Could not find entity with id: 200"
          ],
          "reasonPhrase": "Not Found",
          "statusCode": 404
        }
        """;

    /**
     * JSON example of an error response for HTTP 400 (Bad Request).
     * Includes a list of error messages, the reason phrase, and the status code.
     */
    public static final String BAD_REQUEST_RESPONSE = """
        {
          "errors": [
            "Validation error message"
          ],
          "reasonPhrase": "Bad Request",
          "statusCode": 400
        }
        """;

}
