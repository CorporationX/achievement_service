package faang.school.achievement.dto;

import faang.school.achievement.model.Rarity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AchievementDto {
    private long id;
    private String title;
    private String description;
    private Rarity rarity;
    private LocalDateTime updateAt;
}
