package faang.school.achievement.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        log.error("Validation failed: {}", errors);
        return buildErrorResponseEntity(HttpStatus.BAD_REQUEST, "Validation failed", errors);
    }

    @ExceptionHandler(EventDeserializationException.class)
    public ResponseEntity<Object> handleEventDeserializationException(EventDeserializationException ex) {
        String message = ex.getMessage();
        log.error("Event deserialization: {}", message, ex);
        return buildErrorResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    @ExceptionHandler(EventSerializationException.class)
    public ResponseEntity<Object> handleEventSerializationException(EventSerializationException ex) {
        String message = ex.getMessage();
        log.error("Event serialization: {}", message, ex);
        return buildErrorResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        log.error("Internal server error: {}", ex.getMessage(), ex);
        return buildErrorResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    private ResponseEntity<Object> buildErrorResponseEntity(HttpStatus status, String message) {
        ErrorResponse errorResponse = new ErrorResponse(status.value(), message);
        return ResponseEntity.status(status).body(errorResponse);
    }

    private ResponseEntity<Object> buildErrorResponseEntity(
            HttpStatus status, String message, Map<String, String> errors) {
        ErrorResponse errorResponse = new ErrorResponse(status.value(), message, errors);
        return ResponseEntity.status(status).body(errorResponse);
    }
}
