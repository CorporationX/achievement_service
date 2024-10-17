package faang.school.achievement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AchievementEvent {
    private long id;
    private String title;
    private String description;
    private String rarity;
    private long points;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
