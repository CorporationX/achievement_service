package faang.school.achievement.exception;

import lombok.Getter;

@Getter
public class HandlerException extends RuntimeException {

    private final String handlerName;
    private final Throwable originalCause;

    public HandlerException(String handlerName, String message, Throwable cause) {
        super(message, cause);
        this.handlerName = handlerName;
        this.originalCause = cause;
    }

    public HandlerException(String handlerName, String message) {
        super(message);
        this.handlerName = handlerName;
        this.originalCause = null;
    }

}