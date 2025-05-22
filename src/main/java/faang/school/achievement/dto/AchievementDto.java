package faang.school.achievement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import faang.school.achievement.model.Rarity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AchievementDto {

    private Long id;
    private String title;
    private String description;
    private Rarity rarity;
    private long points;
}