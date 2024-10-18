package faang.school.achievement.dto;

import lombok.Data;

@Data
public class AchievementProgressDto {
    private long id;
    private long achievementId;
    private long userId;
    private long currentPoints;
}
