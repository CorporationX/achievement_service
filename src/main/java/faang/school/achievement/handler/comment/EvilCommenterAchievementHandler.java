package faang.school.achievement.handler.comment;

import faang.school.achievement.config.achievent.AchievementConfiguration;
import faang.school.achievement.config.cache.AchievementCache;
import faang.school.achievement.dto.achievement.comment.NewCommentEventDto;
import faang.school.achievement.handler.AbstractEventHandler;
import faang.school.achievement.service.achievement.AchievementService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EvilCommenterAchievementHandler extends AbstractEventHandler<NewCommentEventDto> {

    public EvilCommenterAchievementHandler(AchievementConfiguration achievementConfiguration,
                                           AchievementService achievementService,
                                           AchievementCache achievementCache) {
        super(achievementConfiguration, achievementService, achievementCache);
    }

    @Async
    @Override
    public void handle(NewCommentEventDto newCommentEventDto) {
        handleAchievement(newCommentEventDto, achievementConfiguration.getEvilCommenter());
    }

    @Override
    public Class<?> getInstance() {
        return NewCommentEventDto.class;
    }
}
