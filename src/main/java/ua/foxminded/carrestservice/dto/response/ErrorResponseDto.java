package ua.foxminded.carrestservice.dto.response;

import java.util.List;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Data Transfer Object (DTO) for representing error responses in the car rest service system.
 * Contains details about an HTTP error, including the reason phrase, status code, and a list of
 * error messages. Uses Lombok {@code @Getter} annotation to generate getter methods for all fields.
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
     * A list of error messages describing the issues encountered.
     */
    private final List<String> errors;

    /**
     * Constructs an {@code ErrorResponseDto} with the specified HTTP status and list of errors.
     *
     * @param httpStatus the {@link HttpStatus} containing the status code and reason phrase
     * @param errors     the list of error messages
     */
    public ErrorResponseDto(HttpStatus httpStatus, List<String> errors) {
        this.reasonPhrase = httpStatus.getReasonPhrase();
        this.statusCode = httpStatus.value();
        this.errors = errors;
    }

}
