package ua.foxminded.carrestservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Abstract base class for custom exceptions in the car rest service system.
 * Extends {@link RuntimeException} to represent unchecked exceptions. Provides an HTTP status code
 * for error handling in REST APIs. Uses Lombok {@code @Getter} annotation to generate getter
 * methods for the HTTP status field.
 *
 * @author Serhii Bohdan
 * @see RuntimeException
 * @see org.springframework.http.HttpStatus
 * @see lombok.Getter
 */
@Getter
public abstract class AbstractCustomException extends RuntimeException {

    /**
     * The HTTP status code associated with the exception.
     */
    private final HttpStatus httpStatus;

    /**
     * Constructs an {@code AbstractCustomException} with the specified HTTP status and message.
     *
     * @param httpStatus the {@link HttpStatus} for the exception
     * @param message    the detail message for the exception
     */
    protected AbstractCustomException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
