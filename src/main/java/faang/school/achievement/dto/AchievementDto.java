package faang.school.achievement.dto;

import faang.school.achievement.model.Rarity;
import lombok.Data;

@Data
public class AchievementDto {
    private long id;
    private String title;
    private String description;
    private Rarity rarity;
    private long points;
}
