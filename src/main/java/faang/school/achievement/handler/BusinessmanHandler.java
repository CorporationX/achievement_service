package faang.school.achievement.handler;

import faang.school.achievement.cache.AchievementCacheRedis;
import faang.school.achievement.event.project.ProjectEvent;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.service.AchievementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BusinessmanHandler extends ProjectEventHandler<ProjectEvent> {

    public BusinessmanHandler(AchievementService achievementService,
                              AchievementProgressRepository achievementProgressRepository,
                              AchievementCacheRedis achievementCache,
                              @Value("${listener.type.achievements.businessman}") String title) {
        super(achievementService, achievementProgressRepository, achievementCache, title);
    }

    @Override
    public void handle(ProjectEvent event) {
        handleEvent(event);
    }
}
