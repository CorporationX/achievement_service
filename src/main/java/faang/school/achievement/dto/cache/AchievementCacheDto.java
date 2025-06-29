package faang.school.achievement.dto.cache;

import faang.school.achievement.model.Rarity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AchievementCacheDto {
    private long id;
    private String title;
    private String description;
    private Rarity rarity;
    private long points;
}
