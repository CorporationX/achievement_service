package faang.school.achievement.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserAchievementDto(
        Long id,
        long userId,
        long achievementId,
        LocalDateTime achievedAt
) {
}