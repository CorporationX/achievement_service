package faang.school.achievement.exception;

public class CustomException extends RuntimeException {

    public CustomException(ExceptionMessage message, Object... args) {
        super(message.formatMessage(args));
    }
}
