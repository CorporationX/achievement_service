package faang.school.achievement.dto.achievement;

import faang.school.achievement.model.Rarity;
import lombok.Data;

@Data
public class AchievementFilterDto {
    private String titlePattern;
    private String descriptionPattern;
    private Rarity rarity;
}
