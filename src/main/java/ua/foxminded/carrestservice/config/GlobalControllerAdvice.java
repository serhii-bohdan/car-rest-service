package ua.foxminded.carrestservice.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.foxminded.carrestservice.dto.response.ErrorResponseDto;
import ua.foxminded.carrestservice.exception.AbstractCustomException;

/**
 * Global exception handler for REST controllers in the car rest service system.
 * Annotated with {@code @RestControllerAdvice} to provide centralized exception handling across
 * all REST controllers. Handles {@link AbstractCustomException} and its subclasses, returning a
 * {@link ResponseEntity} with an {@link ErrorResponseDto} containing the HTTP status and error message.
 *
 * @author Serhii Bohdan
 * @see AbstractCustomException
 * @see ErrorResponseDto
 * @see org.springframework.web.bind.annotation.RestControllerAdvice
 * @see org.springframework.web.bind.annotation.ExceptionHandler
 */
@RestControllerAdvice
public class GlobalControllerAdvice {

    /**
     * Handles {@link AbstractCustomException} and its subclasses.
     * Returns a {@link ResponseEntity} with the HTTP status from the exception and an
     * {@link ErrorResponseDto} containing the status and error message.
     *
     * @param exception the {@link AbstractCustomException} to handle
     * @return a {@link ResponseEntity} containing an {@link ErrorResponseDto}
     */
    @ExceptionHandler({AbstractCustomException.class})
    public ResponseEntity<ErrorResponseDto> handleCustomException(AbstractCustomException exception) {
        return ResponseEntity.status(exception.getHttpStatus())
            .body(new ErrorResponseDto(exception.getHttpStatus(), exception.getMessage()));
    }

}
