package faang.school.achievement.cache;

import faang.school.achievement.model.Achievement;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class AchievementCache {
    private Map<String, Achievement> cache;

    @PostConstruct
    public void createCache() {
        cache = new HashMap<>();
    }

    public void loadAchievements(List<Achievement> achievements) {
        for (Achievement achievement : achievements) {
            cache.put(achievement.getTitle(), achievement);
        }
    }

    public void loadAchievement(Achievement achievement) {
            cache.put(achievement.getTitle(), achievement);
    }

    public Optional<Achievement> getByName(String name) {
        return Optional.ofNullable(cache.get(name));
    }

    public boolean contains(String name) {
        return cache.containsKey(name);
    }
}