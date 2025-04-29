package faang.school.achievement.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
@Getter
public enum ErrorMessage {

    MESSAGE_PROCESSING_FAILED("Failed to process message: %s"),
    ACHIEVEMENT_NOT_FOUND_BY_TITLE("Achievement with title '%s' not found"),
    ACHIEVEMENT_PROGRESS_NOT_FOUND_BY_ID("AchievementProgress with id %d not found"),
    ACHIEVEMENT_PROGRESS_NOT_FOUND_BY_ID_AND_ACHIEVEMENT_ID("Progress not found for userId=%d and achievementId=%d");

    private final String message;
}
