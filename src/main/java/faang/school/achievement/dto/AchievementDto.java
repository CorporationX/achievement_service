package faang.school.achievement.dto;

import faang.school.achievement.model.Rarity;

public record AchievementDto(
        Long id,
        String title,
        String description,
        Rarity rarity,
        Long points
) {
}
