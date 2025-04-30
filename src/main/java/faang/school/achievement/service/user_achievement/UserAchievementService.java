package faang.school.achievement.service.user_achievement;

import org.springframework.transaction.annotation.Transactional;

public interface UserAchievementService {

    @Transactional
    boolean hasAchievement(long userId, long achievementId);

    @Transactional
    void giveAchievement(long userId, long achievementId);
}
