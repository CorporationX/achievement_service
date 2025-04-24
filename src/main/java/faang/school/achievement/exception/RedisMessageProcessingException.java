package faang.school.achievement.exception;

public class RedisMessageProcessingException extends RuntimeException {
    public RedisMessageProcessingException(String message) {
        super(message);
    }

    public RedisMessageProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
