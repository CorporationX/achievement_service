package faang.school.achievement.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}
