package faang.school.achievement.exception;

import faang.school.achievement.dto.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EmptyFilterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleEmptyFilterException(EmptyFilterException ex) {
        log.error("EmptyFilterException occurred: {}", ex.getMessage());
        return ex.getMessage();
    }

    @ExceptionHandler(AchievementNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleAchievementNotFoundException(AchievementNotFoundException ex) {
        log.error("AchievementNotFoundException occurred: {}", ex.getMessage());
        return ex.getMessage();
    }

    @ExceptionHandler({
            AchievementDoesntExistsException.class,
            ProgressNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleExceptionsWithStatusNotFound(Exception ex) {
        return ResponseEntity.status(NOT_FOUND).body(getErrorResponse(ex));
    }

    @ExceptionHandler({
            EventConvertingException.class,
            JsonDeserializationException.class,
    })
    public ResponseEntity<ErrorResponse> handleExceptionsWithStatusBadRequest(Exception ex) {
        return ResponseEntity.status(BAD_REQUEST).body(getErrorResponse(ex));
    }

    private ErrorResponse getErrorResponse(Exception ex) {
        log.error("{}", ex.toString());
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .build();
    }
}
