package faang.school.achievement.dto.achievement;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AchievementProgressDto {
    private Long id;
    private AchievementDto achievement;
    private Long userId;
    private Long currentPoints;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
