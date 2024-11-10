package faang.school.achievement.handler.profile_pic_event;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.handler.AchievementProgressHandler;
import faang.school.achievement.service.AchievementService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Setter
@Component
public class HandsomeAchievementHandler extends ProfileEventHandler {

    @Value("${achievements.profile-achievements.HANDSOME.name}")
    private String title;

    public HandsomeAchievementHandler(AchievementService achievementService,
                                      AchievementCache achievementCache,
                                      AchievementProgressHandler achievementProgressHandler) {
        super(achievementService, achievementCache, achievementProgressHandler);
    }

    @Override
    protected String getAchievementTitle() {
        return title;
    }
}
