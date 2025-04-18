package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
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

    @Transactional
    public void fillCache() {
        Map<String, Achievement> achievementsByTitle = new HashMap<>();
        achievementRepository.findAll()
                .forEach(achievement -> {
                    Hibernate.initialize(achievement.getUserAchievements());
                    achievementsByTitle.put(achievement.getTitle(), achievement);
                });

        achievementRedisService.saveAchievement(achievementsByTitle);
        log.info("Achievements saved in cache");
    }
}