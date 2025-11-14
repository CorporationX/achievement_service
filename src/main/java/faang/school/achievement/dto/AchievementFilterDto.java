package faang.school.achievement.dto;

import faang.school.achievement.model.Rarity;
import jakarta.validation.constraints.Size;

public record AchievementFilterDto(
        @Size(max = 128, message = "Title cannot exceed 128 characters")
        String title,

        @Size(max = 1024, message = "Description cannot exceed 1024 characters")
        String description,

        Rarity rarity
) {
}