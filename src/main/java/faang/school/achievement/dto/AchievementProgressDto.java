package faang.school.achievement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AchievementProgressDto(
        @NotNull @Positive Long achievementId,
        @NotNull @Positive Long userId,
        @NotNull @Min(0) Long currentProgress,
        @NotNull @Positive Long targetProgress
) {
}
