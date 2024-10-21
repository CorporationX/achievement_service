package faang.school.achievement.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AchievementEvent(
        Long userId,
        Long achievementId,
        LocalDateTime createdAt
) {
}