package faang.school.achievement.dto;

import faang.school.achievement.model.Rarity;

public record AchievementResponseDto(
        Long id,
        String title,
        String description,
        Rarity rarity,
        Long points
) {
}