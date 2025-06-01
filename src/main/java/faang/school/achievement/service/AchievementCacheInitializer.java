package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.exception.HandleAchievementException;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.service.interfaces.AchievementRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AchievementCacheInitializer {
    private final AchievementRepository achievementRepository;
    private final AchievementRedisService achievementRedisService;
    private final AchievementMapper achievementMapper;

    @Transactional
    public void fillCache() {
        Map<String, AchievementDto> achievementsByTitle = new HashMap<>();
        achievementRepository.findAll()
                .forEach(achievement -> {
                    Hibernate.initialize(achievement.getUserAchievements());
                    achievementsByTitle.put(achievement.getTitle(), achievementMapper.toDto(achievement));
                });

        try {
            achievementRedisService.saveAchievement(achievementsByTitle);
            log.info("Achievements saved in cache");
        } catch (Exception e) {
            throw new HandleAchievementException("Failed to save achievements to Redis cache", e);
        }
    }
}