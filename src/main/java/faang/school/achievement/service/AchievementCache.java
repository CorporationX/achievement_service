package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class AchievementCache {
    private final AchievementRepository achievementRepository;
    // key - Achievement name
    private Map<String, Achievement> achievementCache;

    @PostConstruct
    public void init() {
        achievementCache = new ConcurrentHashMap<>();
        achievementRepository.findAll().forEach(achievement ->
                achievementCache.put(achievement.getTitle(), achievement));
    }

    public Achievement get(@NotNull String title) {
        if (achievementCache.containsKey(title)) {
            return achievementCache.get(title);
        }
        else {
            Achievement achievement = achievementRepository.findByTitle(title);
            achievementCache.put(title, achievement);
            return achievement;
        }
    }
}
