package faang.school.achievement.achievement_handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.service.AchievementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NiceGuyAchievementHandler extends AbstractEventHandler {
    @Value("${achievement.nice-guy.title}")
    private static final String NICE_GUY = "Nice guy";
    @Value("${achievement.nice-guy.required-progress}")
    private static final int REQUIRED_PROGRESS = 10;

    public NiceGuyAchievementHandler(AchievementCache cache, AchievementService service) {
        super(cache, service, NICE_GUY, REQUIRED_PROGRESS);
    }
}
