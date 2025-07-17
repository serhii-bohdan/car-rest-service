package ua.foxminded.carrestservice.exception;

import java.util.List;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.foxminded.carrestservice.dto.response.ErrorResponseDto;

/**
 * Global exception handler for REST controllers in the car rest service system.
 * Annotated with {@code @RestControllerAdvice} to provide centralized exception handling across
 * all REST controllers. Handles {@link AbstractCustomException} and
 * {@link MethodArgumentNotValidException}, returning a {@link ResponseEntity} with an
 * {@link ErrorResponseDto} containing the HTTP status and error message(s).
 *
 * @author Serhii Bohdan
 * @see AbstractCustomException
 * @see ErrorResponseDto
 * @see org.springframework.web.bind.annotation.RestControllerAdvice
 * @see org.springframework.web.bind.annotation.ExceptionHandler
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link AbstractCustomException} and its subclasses.
     * Returns a {@link ResponseEntity} with the HTTP status from the exception and an
     * {@link ErrorResponseDto} containing the status and error message.
     *
     * @param exception the {@link AbstractCustomException} to handle
     * @return a {@link ResponseEntity} containing an {@link ErrorResponseDto} with a single error message
     */
    @ExceptionHandler({AbstractCustomException.class})
    public ResponseEntity<ErrorResponseDto> handleCustomException(AbstractCustomException exception) {
        return ResponseEntity.status(exception.getHttpStatus())
            .body(new ErrorResponseDto(exception.getHttpStatus(), List.of(exception.getMessage())));
    }

    /**
     * Handles {@link MethodArgumentNotValidException} for invalid input data.
     * Extracts field error messages and returns a {@link ResponseEntity} with an
     * {@link ErrorResponseDto} containing the HTTP status and a list of validation errors.
     *
     * @param exception the {@link MethodArgumentNotValidException} to handle
     * @return a {@link ResponseEntity} containing an {@link ErrorResponseDto} with validation errors
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        HttpStatus httpStatus = statusCodeToHttpStatus(exception.getStatusCode());
        List<String> errors = exception.getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .toList();

        return ResponseEntity.status(httpStatus)
            .body(new ErrorResponseDto(httpStatus, errors));
    }

    private HttpStatus statusCodeToHttpStatus(HttpStatusCode httpStatusCode) {
        return HttpStatus.valueOf(httpStatusCode.value());
    }

}
