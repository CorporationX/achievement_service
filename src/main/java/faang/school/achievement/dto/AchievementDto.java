package faang.school.achievement.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import faang.school.achievement.model.Rarity;
import lombok.Builder;

@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record AchievementDto(long id, String title, String description, Rarity rarity, long points) {
}
