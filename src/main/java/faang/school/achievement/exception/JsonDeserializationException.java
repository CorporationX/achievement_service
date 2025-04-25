package faang.school.achievement.exception;

public class JsonDeserializationException extends RuntimeException {
    public JsonDeserializationException(String message) {
        super(message);
    }

    public JsonDeserializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
