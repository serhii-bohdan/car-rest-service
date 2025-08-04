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
     * Constructs a new {@code EntityNotFoundException} with the specified message.
     * Sets the HTTP status code to {@link HttpStatus#NOT_FOUND} (404).
     *
     * @param message the detail message explaining why the entity was not found
     */
    public EntityNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

}
