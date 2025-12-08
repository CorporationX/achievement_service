package faang.school.achievement.exception;

import lombok.Getter;

@Getter
public class HandlerException extends RuntimeException {

    private final String handlerName;

    public HandlerException(String handlerName, String message, Throwable cause) {
        super(message, cause);
        this.handlerName = handlerName;
    }

    public HandlerException(String handlerName, String message) {
        super(message);
        this.handlerName = handlerName;
    }
}