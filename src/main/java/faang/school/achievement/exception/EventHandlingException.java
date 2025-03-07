package faang.school.achievement.exception;

public class EventHandlingException extends RuntimeException {

    public EventHandlingException(String message) {
        super(message);
    }

    public EventHandlingException(String message, Throwable cause) {
        super(message, cause);
    }
}
