package faang.school.achievement.util.cache;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class AchievementCache {

    private final Map<String, Achievement> achievements = new ConcurrentHashMap<>();
    private final AchievementRepository achievementRepository;

    @PostConstruct
    public void fillCache() {
        Iterable<Achievement> entities = achievementRepository.findAll();
        for (Achievement achievement : entities) {
            achievements.put(achievement.getTitle(), achievement);
        }
    }

    public Achievement get(String title) {
        return achievements.get(title);
    }
}
