package faang.school.achievement.cache;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class AchievementCache {
    private final AchievementRepository achievementRepository;
    private Map<String, Achievement> cashedAchievement = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        List<Achievement> allAchievement = (List<Achievement>) achievementRepository.findAll();
        for (Achievement achievement : allAchievement) {
            cashedAchievement.put(achievement.getTitle(), achievement);
        }
        log.info("Cache success initializing");
    }

    public Achievement get(String title) {
        Achievement achievement = cashedAchievement.get(title);
        log.info("Getting achievement with title {}", achievement.getTitle());
       return achievement;
    }
}
