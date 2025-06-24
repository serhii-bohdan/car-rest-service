package ua.foxminded.carrestservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when an entity is not found in the car rest service system.
 * Extends {@link AbstractCustomException} to provide a specific HTTP status code and message for
 * entity not found errors. Uses Lombok's {@code @Getter} annotation to generate getter methods
 * for inherited fields.
 *
 * @author Serhii Bohdan
 * @see AbstractCustomException
 * @see org.springframework.http.HttpStatus
 * @see lombok.Getter
 */
@Getter
public class EntityNotFoundException extends AbstractCustomException {

    /**
     * Constructs an {@code EntityNotFoundException} with the specified HTTP status and message.
     *
     * @param httpStatus the {@link HttpStatus} for the exception, typically {@code NOT_FOUND}
     * @param message    the detail message describing the missing entity
     */
    public EntityNotFoundException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

}
