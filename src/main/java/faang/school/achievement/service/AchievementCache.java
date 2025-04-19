package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AchievementCache {
    private final Map<String, Achievement> achievementCache = new HashMap<>();
    private final AchievementRepository repository;

    @PostConstruct
    private void init() {
        repository.findAll().forEach(achievement -> {
            achievementCache.put(achievement.getTitle(), achievement);
        });
        log.info("Кеш достижений заполнен.");
    }

    public Achievement get(String title) {
        return achievementCache.get(title);
    }

}

