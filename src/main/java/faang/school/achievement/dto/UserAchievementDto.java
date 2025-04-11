package faang.school.achievement.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record UserAchievementDto(long id, AchievementDto achievement) {
}
