package faang.school.achievement.exception;

public class AchievementNotFoundException extends RuntimeException {

    public AchievementNotFoundException(String message, Object... args) {
        super(String.format(message, args));
    }
}
