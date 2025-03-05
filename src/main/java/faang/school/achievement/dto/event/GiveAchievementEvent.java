package faang.school.achievement.dto.event;

import faang.school.achievement.model.UserAchievement;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class GiveAchievementEvent extends ApplicationEvent {
    private UserAchievement userAchievement;

    public GiveAchievementEvent(Object source, UserAchievement userAchievement) {
        super(source);
        this.userAchievement = userAchievement;
    }
}
