package faang.school.achievement.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class AchievementEvent extends Event {
    private Long userId;
    private Long achievementId;

    public AchievementEvent(LocalDateTime eventTime, Long userId, Long achievementId) {
        super(eventTime);
        this.userId = userId;
        this.achievementId = achievementId;
    }
}
