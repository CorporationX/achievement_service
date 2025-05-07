package faang.school.achievement.exeption;

public class ProgressNotFound extends RuntimeException {

    public final static String MESSAGE_TEMPLATE = "Progress not found for user";

    public ProgressNotFound() {
        super(MESSAGE_TEMPLATE);
    }
}
