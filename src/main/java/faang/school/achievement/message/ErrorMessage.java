package faang.school.achievement.message;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
public enum ErrorMessage {

    MESSAGE_PROCESSING_FAILED("Failed to process message: %s", 1),
    ACHIEVEMENT_NOT_FOUND_BY_TITLE("Achievement with title '%s' not found", 1),
    ACHIEVEMENT_PROGRESS_NOT_FOUND_BY_ID("AchievementProgress with id %d not found", 1),
    ACHIEVEMENT_PROGRESS_NOT_FOUND_BY_ID_AND_ACHIEVEMENT_ID("Progress not found for userId=%d and achievementId=%d", 2);

    private final String template;
    private final int argCount;

    public String format(Object... args) {
        if (args.length != argCount) {
            throw new IllegalArgumentException(
                    String.format("Expected %d arguments, but got %d for message: %s", argCount, args.length, name())
            );
        }
        return String.format(template, args);
    }
}
