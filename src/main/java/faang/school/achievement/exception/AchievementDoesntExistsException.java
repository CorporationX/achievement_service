package faang.school.achievement.exception;

public class AchievementDoesntExistsException extends RuntimeException {

    public AchievementDoesntExistsException(String message, Object... args) {
        super(String.format(message, args));
    }
}
