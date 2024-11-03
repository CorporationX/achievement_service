package faang.school.achievement.handler.picture;

import faang.school.achievement.config.achievent.AchievementConfiguration;
import faang.school.achievement.dto.achievement.profile.ProfilePicEvent;
import faang.school.achievement.handler.AbstractEventHandler;
import faang.school.achievement.repository.RedisRepository;
import faang.school.achievement.service.achievement.AchievementService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class HandsomeAchievementHandler extends AbstractEventHandler<ProfilePicEvent> {

    public HandsomeAchievementHandler(AchievementConfiguration achievementConfiguration,
                                      AchievementService achievementService,
                                      RedisRepository redisRepository) {
        super(achievementConfiguration, achievementService, redisRepository);
    }

    @Async("executor")
    @Override
    public void handle(ProfilePicEvent profilePicEvent) {
        handleAchievement(profilePicEvent, achievementConfiguration.getHandsome());
    }

    @Override
    public Class<?> getInstance() {
        return ProfilePicEvent.class;
    }
}
