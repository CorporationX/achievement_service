package faang.school.achievement.exception;

public class ListenerProcessEventException extends RuntimeException {
    public ListenerProcessEventException(String message) {
        super(message);
    }

    public ListenerProcessEventException(String message, Throwable cause) {
        super(message, cause);
    }
}
