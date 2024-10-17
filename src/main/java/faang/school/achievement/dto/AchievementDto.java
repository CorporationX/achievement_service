package faang.school.achievement.dto;

import faang.school.achievement.model.Rarity;
import lombok.Data;

import java.util.List;

@Data
public class AchievementDto {
    private Long id;
    private String title;
    private String description;
    private Rarity rarity;
    private Long points;
    private List<AchievementProgressDto> progresses;
}
