package faang.school.achievement.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class HandlersException extends RuntimeException {

    private final List<HandlerException> handlerExceptions;

    public HandlersException(String message, List<HandlerException> handlerExceptions) {
        super(message);
        this.handlerExceptions = List.copyOf(handlerExceptions);
    }

}