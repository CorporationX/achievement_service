package faang.school.achievement.achievement_handler;

import faang.school.achievement.dto.event.RecommendationEvent;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.service.AchievementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NiceGuyAchievementHandler extends AbstractEventHandler<RecommendationEvent> {
    public NiceGuyAchievementHandler(AchievementService service,
                                     @Value("${achievement.titles.nice-guy}") String title,
                                     @Value("${achievement.required-progress.nice-guy}") int requiredProgress,
                                     AchievementRepository repository
    ) {
        super(service, title, requiredProgress, repository);
    }
}
