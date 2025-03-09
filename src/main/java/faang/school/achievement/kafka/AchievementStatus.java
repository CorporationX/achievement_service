package faang.school.achievement.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AchievementStatus {

    COLLECTOR("COLLECTOR");

    private final String statusTitle;
}
