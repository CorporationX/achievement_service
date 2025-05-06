package faang.school.achievement.service.cache;

import faang.school.achievement.dto.AchievementDto;

public interface AchievementCacheService {

    AchievementDto getAchievement(String title);
}
