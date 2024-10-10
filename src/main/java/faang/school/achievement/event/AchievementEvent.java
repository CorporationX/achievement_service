package faang.school.achievement.event;

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
    private String title;
    private String description;

    public AchievementEvent(LocalDateTime eventTime, Long userId, Long achievementId, String title, String description) {
        super(eventTime);
        this.userId = userId;
        this.achievementId = achievementId;
        this.title = title;
        this.description = description;
    }
}
