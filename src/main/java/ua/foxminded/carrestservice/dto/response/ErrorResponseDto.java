package ua.foxminded.carrestservice.dto.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Data Transfer Object (DTO) for representing error responses in the car rest service system.
 * Contains details about an HTTP error, including the reason phrase, status code, and a custom
 * message. Uses Lombok {@code @Getter} annotation to generate getter methods for all fields.
 *
 * @author Serhii Bohdan
 * @see org.springframework.http.HttpStatus
 * @see lombok.Getter
 */
@Getter
public class ErrorResponseDto {

    /**
     * The reason phrase associated with the HTTP status code.
     */
    private final String reasonPhrase;

    /**
     * The numeric HTTP status code.
     */
    private final Integer statusCode;

    /**
     * A custom message describing the error.
     */
    private final String message;

    /**
     * Constructs an {@code ErrorResponseDto} with the specified HTTP status and message.
     *
     * @param httpStatus the {@link HttpStatus} providing the reason phrase and status code
     * @param message    the custom error message
     */
    public ErrorResponseDto(HttpStatus httpStatus, String message) {
        this.reasonPhrase = httpStatus.getReasonPhrase();
        this.statusCode = httpStatus.value();
        this.message = message;
    }

}
