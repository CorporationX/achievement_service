package faang.school.achievement.dto;

import faang.school.achievement.model.Rarity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AchievementProgressDto {

    private Long id;
    private String title;
    private String description;
    private Rarity rarity;
    private Integer currentsPoints;
    private Integer totalPoints;
}