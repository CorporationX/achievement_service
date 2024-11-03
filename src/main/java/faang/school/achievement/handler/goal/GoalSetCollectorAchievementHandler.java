package faang.school.achievement.handler.goal;

import faang.school.achievement.config.achievent.AchievementConfiguration;
import faang.school.achievement.dto.goal.GoalSetEventDto;
import faang.school.achievement.handler.AbstractEventHandler;
import faang.school.achievement.repository.RedisRepository;
import faang.school.achievement.service.achievement.AchievementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GoalSetCollectorAchievementHandler extends AbstractEventHandler<GoalSetEventDto> {

    public GoalSetCollectorAchievementHandler(
            AchievementConfiguration achievementConfiguration,
            AchievementService achievementService,
            RedisRepository redisRepository) {
        super(achievementConfiguration, achievementService, redisRepository);
    }

    @Async("executor")
    @Override
    public void handle(GoalSetEventDto goalSetEventDto) {
        handleAchievement(goalSetEventDto, achievementConfiguration.getCollector());
    }

    @Override
    public Class<?> getInstance() {
        return GoalSetEventDto.class;
    }
}
