package ua.foxminded.carrestservice.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when an OAuth access token cannot be obtained in the car rest service system.
 * Extends {@link AbstractCustomException} to provide a specific HTTP status code and message for
 * access token retrieval errors. Uses Lombok's {@code @Getter} annotation to generate getter methods
 * for inherited fields.
 *
 * @author Serhii Bohdan
 * @see AbstractCustomException
 * @see org.springframework.http.HttpStatus
 */
public class AccessTokenObtainedException extends AbstractCustomException {

    /**
     * Constructs a new {@code AccessTokenObtainedException} with the specified message.
     * Sets the HTTP status code to {@link HttpStatus#BAD_REQUEST} (400).
     *
     * @param message the detail message explaining why the access token could not be obtained
     */
    public AccessTokenObtainedException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

}
