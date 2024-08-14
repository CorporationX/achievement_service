package faang.school.achievement.cache;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Evgenii Malkov
 */
@Component
@RequiredArgsConstructor
public class AchievementCache {
    private final Map<String, Achievement> achievements = new HashMap<>();
    private final AchievementRepository achievementRepository;

    @PostConstruct
    public void fillAchievements() {
        List<Achievement> allAchievements = achievementRepository.findAll();
        allAchievements.forEach((achieve) -> achievements.put(achieve.getTitle(), achieve));
    }

    public Achievement get(String title) {
        return this.achievements.get(title);
    }
}
