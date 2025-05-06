package faang.school.achievement.messaging.handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.SkillAcquiredEvent;
import faang.school.achievement.model.AchievementType;
import faang.school.achievement.service.AchievementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WhoeverAchievementHandler extends SkillEventHandler {

    private static final String ACHIEVE_TITLE = AchievementType.SKILL_KEEPER.getTitle();

    public WhoeverAchievementHandler(
            AchievementService achievementService,
            AchievementCache achievementCache
    ) {
        super(ACHIEVE_TITLE, achievementService, achievementCache);
    }

    @Override
    public void handleEvent(SkillAcquiredEvent event) {
        log.info("Handling event for achievement: {}", ACHIEVE_TITLE);
        super.handleEvent(event);
    }
}
