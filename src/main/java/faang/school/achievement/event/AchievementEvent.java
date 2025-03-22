package faang.school.achievement.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import faang.school.achievement.model.Rarity;
import lombok.Data;

import java.io.Serializable;

@Data
public class AchievementEvent implements Serializable {
    @JsonProperty("achievement_id")
    private Long id;

    @JsonProperty("achievement_title")
    private String title;

    @JsonProperty("achievement_description")
    private String description;

    @JsonProperty("achievement_rarity")
    private Rarity rarity;
}
