package faang.school.achievement.dto.achievement;

import faang.school.achievement.model.Rarity;

public record AchievementFilterDto(
        String titlePattern,
        String descriptionPattern,
        Rarity rarity
) {
}
