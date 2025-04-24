package faang.school.achievement.exception;

public class RedisContainerIsEmptyException extends RuntimeException {
    public RedisContainerIsEmptyException(String message) {
        super(message);
    }
}
