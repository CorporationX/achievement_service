package faang.school.achievement.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import faang.school.achievement.model.Rarity;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record AchievementFilterDto(String title, String description, Rarity rarity) {
}
