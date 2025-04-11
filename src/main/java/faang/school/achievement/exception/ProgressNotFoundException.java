package faang.school.achievement.exception;

public class ProgressNotFoundException extends RuntimeException {

    public ProgressNotFoundException(String message, Object... args) {
        super(String.format(message, args));
    }
}
