package faang.school.achievement.service;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.handler.EntityHandler;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AchievementService {
    private final EntityHandler entityHandler;
    private final AchievementCache achievementCache;
    private final AchievementMapper achievementMapper;
    private final AchievementRepository achievementRepository;

    @Transactional(readOnly = true)
    public AchievementDto getAchievementByTitle(String achievementTitle) {
        Achievement achievement =  achievementCache.get(achievementTitle).orElseGet(() -> {
            Achievement achievementFromDataBase = entityHandler.getOrThrowException(Achievement.class, achievementTitle,
                    () -> achievementRepository.findByTitle(achievementTitle));
            achievementCache.cacheAchievement(achievementFromDataBase);
            return achievementFromDataBase;
        });
        return achievementMapper.toDto(achievement);
    }
}
