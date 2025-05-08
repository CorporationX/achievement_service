package faang.school.achievement.exception.handler;

import faang.school.achievement.dto.ExceptionResponseDto;
import faang.school.achievement.exception.MentorshipEventDeserializationException;
import faang.school.achievement.exception.MentorshipEventValidationException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionResponseDto> handleNoSuchElementException(NoSuchElementException ex) {
        log.warn("Entity not found: {}", ex.getMessage());

        ExceptionResponseDto exceptionResponse = ExceptionResponseDto.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .errorCode("NOT_FOUND")
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.warn("Entity not found: {}", ex.getMessage());

        ExceptionResponseDto exceptionResponse = ExceptionResponseDto.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .errorCode("ENTITY_NOT_FOUND")
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }

    @ExceptionHandler(MentorshipEventDeserializationException.class)
    public ResponseEntity<ExceptionResponseDto> handleMentorshipEventDeserializationException(MentorshipEventDeserializationException ex) {
        log.error("Deserialization error: ", ex);

        ExceptionResponseDto exceptionResponse = ExceptionResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .errorCode("DESERIALIZATION_ERROR")
                .message("Failed to deserialize the mentorship event: " + ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler(MentorshipEventValidationException.class)
    public ResponseEntity<ExceptionResponseDto> handleMentorshipEventValidationException(MentorshipEventValidationException ex) {
        log.error("Validation error: ", ex);

        ExceptionResponseDto exceptionResponse = ExceptionResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .errorCode("VALIDATION_ERROR")
                .message("Validation failed: " + ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }
}
