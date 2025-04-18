package faang.school.achievement.exception;

public class HandleAchievementException extends RuntimeException {
    public HandleAchievementException(String message) {
        super(message);
    }

    public HandleAchievementException(String message, Throwable cause) {
        super(message, cause);
    }
}
