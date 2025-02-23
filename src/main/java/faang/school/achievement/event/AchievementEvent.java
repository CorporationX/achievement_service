package faang.school.achievement.event;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AchievementEvent(
        @NotBlank Long userId,
        @NotBlank Long achievementId,
        @NotBlank LocalDateTime achievementDateTime
) {
}
