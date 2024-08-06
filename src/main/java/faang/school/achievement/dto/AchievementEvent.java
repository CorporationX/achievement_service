package faang.school.achievement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AchievementEvent {
    Long id;
    Long achievementId;
    Long userId;
    LocalDateTime achievementDttm;
}
