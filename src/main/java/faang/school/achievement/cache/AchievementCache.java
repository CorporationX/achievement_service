package faang.school.achievement.cache;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class AchievementCache {
    private final AchievementRepository achievementRepository;
    private final Map<String, Achievement> cache = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        achievementRepository.findAll().forEach(achievement ->
                cache.put(achievement.getTitle(), achievement));
    }

    public Optional<Achievement> get(String title) {
        return Optional.ofNullable(cache.get(title));
    }
}
