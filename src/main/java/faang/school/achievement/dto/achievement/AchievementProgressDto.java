package faang.school.achievement.dto.achievement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AchievementProgressDto {
    private long id;
    private long achievementId;
    private long userId;
    private long currentPoints;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long version;
}
