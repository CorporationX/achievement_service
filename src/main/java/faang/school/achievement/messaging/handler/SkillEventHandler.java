package faang.school.achievement.messaging.handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.SkillAcquiredEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.service.AchievementEventService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

@Slf4j
@AllArgsConstructor
public abstract class SkillEventHandler implements EventSkillHandler {

    private final String achievementName;
    private final AchievementEventService achievementEventService;
    private final AchievementCache achievementCache;

    @Async
    public void handleEvent(SkillAcquiredEvent event) {
        log.info("HandleEvent is running in thread: {}", Thread.currentThread().getName());
        Achievement achievement = achievementCache.get(achievementName);
        achievementEventService.processAchievementAcquired(event, achievement);
    }
}
