package faang.school.achievement.message;

import faang.school.achievement.service.AchievementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EvilCommenterAchievementHandler extends CommentEventHandler {

    public EvilCommenterAchievementHandler(AchievementService achievementService,
                                           @Value("${achievement.evil-commenter.title}") String achievementTitle) {
        super(achievementService, achievementTitle);
    }

}
