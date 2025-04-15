package faang.school.achievement.exception;

public class EventConvertingException extends RuntimeException {

    public EventConvertingException(String message, Object... args) {
        super(String.format(message, args));
    }
}
