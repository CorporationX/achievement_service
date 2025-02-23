package faang.school.achievement.dto;

import lombok.Builder;

@Builder
public record AchievementDto(
        Long id,
        String title,
        String description,
        Long points) {
}
