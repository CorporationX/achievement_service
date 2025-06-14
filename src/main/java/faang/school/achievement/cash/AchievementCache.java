package faang.school.achievement.cash;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class AchievementCache {

    private final AchievementRepository achievementRepository;
    private final Map<String, Achievement> cache = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        log.info("Loading <AchievementCache>...");
        achievementRepository.findAll().forEach(achievement -> cache.put(achievement.getTitle(), achievement));
        log.info("Loaded <AchievementCache>: {}", cache.size());
    }

    public Achievement getByTitle(String title) {
        Achievement achievement = cache.get(title);
        if (achievement == null) {
            throw new IllegalArgumentException("Achievement not found in cache: " + title);
        }
        return achievement;
    }
}
