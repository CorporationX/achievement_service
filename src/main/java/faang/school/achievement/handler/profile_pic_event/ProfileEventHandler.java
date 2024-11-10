package faang.school.achievement.handler.profile_pic_event;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.ProfileEventDto;
import faang.school.achievement.handler.AbstractEventHandler;
import faang.school.achievement.handler.AchievementProgressHandler;
import faang.school.achievement.service.AchievementService;

public abstract class ProfileEventHandler extends AbstractEventHandler<ProfileEventDto> {

    public ProfileEventHandler(AchievementService achievementService,
                               AchievementCache achievementCache,
                               AchievementProgressHandler achievementProgressHandler) {
        super(achievementService, achievementCache, achievementProgressHandler);
    }

    @Override
    public void handle(ProfileEventDto event) {
        handleAchievement(event.userId(), getAchievementTitle());
    }
}
