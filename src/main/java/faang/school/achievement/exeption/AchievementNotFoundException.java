package faang.school.achievement.exeption;

public class AchievementNotFoundException extends RuntimeException {

    public static final String MESSAGE_TEMPLATE = "Achievement not found";

    public AchievementNotFoundException() {
        super(MESSAGE_TEMPLATE);
    }
}
