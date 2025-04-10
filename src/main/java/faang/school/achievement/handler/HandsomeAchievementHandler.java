package faang.school.achievement.handler;

import faang.school.achievement.event.ProfilePicEvent;
import faang.school.achievement.repository.adapter.AchievementRepositoryAdapter;
import faang.school.achievement.service.AchievementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HandsomeAchievementHandler extends AbstractAchievementHandler<ProfilePicEvent> {

    public HandsomeAchievementHandler(AchievementService achievementService,
                                      AchievementRepositoryAdapter achievementRepositoryAdapter,
                                      @Value("${achievement.title.handsome}") String handsomeAchievementTitle) {
        super(achievementService, achievementRepositoryAdapter, handsomeAchievementTitle);
    }

    @Override
    public long getUserId(ProfilePicEvent event) {
        return event.userId();
    }
}
