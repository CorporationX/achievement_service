package faang.school.achievement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import faang.school.achievement.model.Rarity;

public record AchievementDto(
        @JsonProperty("id") Long id,
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("rarity") Rarity rarity,
        @JsonProperty("points") Long points
) {
}

