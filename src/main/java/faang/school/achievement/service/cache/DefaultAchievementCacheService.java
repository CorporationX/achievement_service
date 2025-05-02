package faang.school.achievement.service.cache;

import faang.school.achievement.dto.AchievementDto;
import org.springframework.stereotype.Service;

@Service
public class DefaultAchievementCacheService implements AchievementCacheService {

    @Override
    public AchievementDto getAchievement(String title) {
        //todo логика реализуется в другой задаче
        return null;
    }
}
