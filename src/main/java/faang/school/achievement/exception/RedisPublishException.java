package faang.school.achievement.exception;

public class RedisPublishException extends RuntimeException {
    public RedisPublishException(String message) {
        super(message);
    }

    public RedisPublishException(String message, Throwable cause) {
        super(message, cause);
    }
}
