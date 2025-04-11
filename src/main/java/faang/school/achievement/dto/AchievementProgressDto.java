package faang.school.achievement.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record AchievementProgressDto(long id, long currentPoints, AchievementDto achievement) {
}
