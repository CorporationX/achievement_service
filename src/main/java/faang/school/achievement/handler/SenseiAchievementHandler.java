package faang.school.achievement.handler;

import faang.school.achievement.model.event.MentorshipStartEvent;
import faang.school.achievement.service.AchievementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SenseiAchievementHandler extends AbstractEventHandler<MentorshipStartEvent> {

    public SenseiAchievementHandler(/*AchievementCache achievementCache,*/
                                    AchievementService achievementService,
                                    @Value("${achievement-titles.sensei}") String achievementTitle) {
        super(/*achievementCache,*/ achievementService, achievementTitle);
    }
}
