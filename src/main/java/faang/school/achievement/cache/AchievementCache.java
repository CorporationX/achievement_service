package faang.school.achievement.cache;

import faang.school.achievement.model.Achievement;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AchievementCache {
    private final Map<String, Achievement> cache = new HashMap<>();

    public void loadAchievements(List<Achievement> achievements) {
        for (Achievement achievement : achievements) {
            cache.put(achievement.getTitle(), achievement);
        }
    }

    public void loadAchievement(Achievement achievement) {
            cache.put(achievement.getTitle(), achievement);
    }

    public Achievement getByName(String name) {
        return cache.get(name);
    }

    public boolean contains(String name) {
        return cache.containsKey(name);
    }
}