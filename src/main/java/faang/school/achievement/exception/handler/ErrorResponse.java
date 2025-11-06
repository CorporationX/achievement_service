package faang.school.achievement.exception.handler;

public record ErrorResponse(
        String error,
        String message
) {
    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public ErrorResponse(String message) {
        this(null, message);
    }
}