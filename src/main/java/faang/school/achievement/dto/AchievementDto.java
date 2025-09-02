package faang.school.achievement.dto;

import lombok.Builder;

@Builder
public record AchievementDto(
        long id,
        AchievementType title,
        long points
) {
}
