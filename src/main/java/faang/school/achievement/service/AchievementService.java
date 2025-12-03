package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;

public interface AchievementService {
    void operationAchievement(long userId, String achievementTitle);

    void giveAchievement(Long userId, Achievement achievement);

    long incrementAndSaveProgress(long userId, Long achievementId);


}
