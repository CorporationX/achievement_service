package faang.school.achievement.exception.exceptionhandler;

import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.exception.HandleAchievementException;
import faang.school.achievement.exception.ListenerProcessEventException;
import faang.school.achievement.exception.RedisContainerIsEmptyException;
import faang.school.achievement.exception.RedisMessageProcessingException;
import faang.school.achievement.exception.RedisPublishException;
import faang.school.achievement.exception.UnsupportedEventException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("EntityNotFoundException: {}", e.getClass().getSimpleName(), e);
        return buildResponse(e);
    }

    @ExceptionHandler(HandleAchievementException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleHandleAchievementException(HandleAchievementException e) {
        log.error("HandleAchievementException: {}", e.getClass().getSimpleName(), e);
        return buildResponse(e);
    }

    @ExceptionHandler(ListenerProcessEventException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleListenerProcessEventException(ListenerProcessEventException e) {
        log.error("ListenerProcessEventException: {}", e.getClass().getSimpleName(), e);
        return buildResponse(e);
    }

    @ExceptionHandler(RedisContainerIsEmptyException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleRedisContainerIsEmptyException(RedisContainerIsEmptyException e) {
        log.error("RedisContainerIsEmptyException: {}", e.getClass().getSimpleName(), e);
        return buildResponse(e);
    }

    @ExceptionHandler(RedisMessageProcessingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleRedisMessageProcessingException(RedisMessageProcessingException e) {
        log.error("RedisMessageProcessingException: {}", e.getClass().getSimpleName(), e);
        return buildResponse(e);
    }

    @ExceptionHandler(RedisPublishException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleRedisPublishException(RedisPublishException e) {
        log.error("RedisPublishException: {}", e.getClass().getSimpleName(), e);
        return buildResponse(e);
    }

    @ExceptionHandler(UnsupportedEventException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnsupportedEventException(UnsupportedEventException e) {
        log.error("UnsupportedEventException: {}", e.getClass().getSimpleName(), e);
        return buildResponse(e);
    }

    private ErrorResponse buildResponse(Exception e) {
        log.error(e.getClass().getSimpleName(), e);
        return ErrorResponse.builder()
                .timeStamp(LocalDateTime.now())
                .error(e.getClass().getName())
                .message(e.getMessage())
                .build();
    }
}
