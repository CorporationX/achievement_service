package faang.school.achievement.cache;

import faang.school.achievement.dto.AchievementDto;

public interface AchievementCache {

    AchievementDto get(String title);
}
