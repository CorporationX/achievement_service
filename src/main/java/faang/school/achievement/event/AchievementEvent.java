package faang.school.achievement.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AchievementEvent extends Event {
    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("achievementId")
    private Long achievementId;

    @JsonProperty("achievementName")
    private String achievementName;

    @JsonProperty("description")
    private String description;

    @JsonProperty("rarity")
    private Integer rarity;

    @JsonProperty("points")
    private Integer points;

    public AchievementEvent(Long userId, Long achievementId, String achievementName,
                            String description, Integer rarity, Integer points) {
        super("ACHIEVEMENT");
        this.userId = userId;
        this.achievementId = achievementId;
        this.achievementName = achievementName;
        this.description = description;
        this.rarity = rarity;
        this.points = points;
    }
}
