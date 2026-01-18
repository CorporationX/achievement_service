package faang.school.achievement.cache;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AchievementCache {
    private final AchievementRepository achievementRepository;
    private Map<String, Achievement> cachedAchievements;

    @PostConstruct
    private void init() {
        log.info("Loading achievements into cache...");

        Map<String, Achievement> map = new HashMap<>();
        achievementRepository.findAll().forEach(a -> map.put(a.getTitle(), a));
        cachedAchievements = Map.copyOf(map);
    }

    public Achievement getOrThrow(String code) {
        Achievement achievement = cachedAchievements.get(code);

        if (achievement == null) {
            throw new IllegalStateException("Achievement with code '" + code + "' not found in cache");
        }
        return achievement;
    }
}