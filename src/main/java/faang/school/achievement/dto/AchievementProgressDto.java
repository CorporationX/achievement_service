package faang.school.achievement.dto;

import lombok.Builder;

@Builder
public record AchievementProgressDto(
        Long id,
        Long achievementId,
        Long userId,
        Long currentPoints
) {
}
