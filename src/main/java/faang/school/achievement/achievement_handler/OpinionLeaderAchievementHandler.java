package faang.school.achievement.achievement_handler;

import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.service.AchievementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpinionLeaderAchievementHandler extends AbstractEventHandler {

    public OpinionLeaderAchievementHandler(AchievementService service,
                                           @Value("${achievements.opinion-leader.title}") String achievementName,
                                           @Value("${achievements.opinion-leader.required-progress}")
                                           int requiredProgress,
                                           AchievementRepository achievementRepository) {
        super(service, achievementName, requiredProgress, achievementRepository);
    }
}
