package faang.school.achievement.handler;

import faang.school.achievement.event.ProfilePicEvent;
import faang.school.achievement.repository.EventKeyCacheRepository;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.cache.AchievementCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HandsomeAchievementHandler extends AbstractAchievementHandler<ProfilePicEvent> {

    public HandsomeAchievementHandler(AchievementService achievementService,
                                      AchievementCache achievementCache,
                                      EventKeyCacheRepository eventKeyCacheRepository,
                                      @Value("${achievement.title.handsome}") String handsomeAchievementTitle) {
        super(achievementService, achievementCache, eventKeyCacheRepository, handsomeAchievementTitle);
    }

    @Override
    public long getUserId(ProfilePicEvent event) {
        return event.userId();
    }

    @Override
    public String generateUniqueEventKey(ProfilePicEvent event) {
        return event.profilePicKey();
    }
}
