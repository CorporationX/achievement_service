package faang.school.achievement.dto.achievement;

import faang.school.achievement.model.Rarity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AchievementDto implements Serializable {
    private long id;
    private String title;
    private String description;
    private Rarity rarity;
    private long points;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
