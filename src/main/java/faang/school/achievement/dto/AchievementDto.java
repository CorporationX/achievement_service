package faang.school.achievement.dto;

import faang.school.achievement.model.Rarity;

public record AchievementDto(
        long id,
        String title,
        String description,
        Rarity rarity,
        long points
) {}