package faang.school.achievement.dto;

import faang.school.achievement.model.Rarity;

public record AchievementFilterDto(String title, String description, Rarity rarity) {
}
