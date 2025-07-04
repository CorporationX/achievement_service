package faang.school.achievement.cache;

import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class AchievementCacheImpl implements AchievementCache {
    private final AchievementRepository achievementRepository;
    private final Map<String, Achievement> cache = new ConcurrentHashMap<>();
    private final AchievementMapper achievementMapper;

    @PostConstruct
    @Transactional
    private void init() {
        for (Achievement achievement : achievementRepository.findAll()) {
            cache.put(achievement.getTitle(), achievement);
        }
    }

    @Override
    public Optional<Achievement> get(String title) {
        return Optional.ofNullable(cache.get(title));
    }
}