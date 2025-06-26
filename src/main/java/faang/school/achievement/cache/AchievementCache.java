package faang.school.achievement.cache;

import faang.school.achievement.dto.AchievementDto;

import java.util.Optional;

public interface AchievementCache {

    Optional<AchievementDto> get(String title);
}
