package faang.school.achievement.redis.cache;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RequiredArgsConstructor
@Component
public class AchievementCacheImpl implements AchievementCache {
    private final AchievementRepository repository;
    private final ConcurrentMap<Long, Achievement> cache = new ConcurrentHashMap<>();
    @PostConstruct
    public void init() {
        Iterable<Achievement> achievements = repository.findAll();
        achievements.forEach(this::put);
    }

    public Achievement get(long id) {
        if (!cache.containsKey(id)) {
            Achievement achievement = repository.findByIdOrThrow(id);
            put(achievement);
        }
        return cache.get(id);
    }

    public Map<Long, Achievement> getAll() {
        return new HashMap<>(cache);
    }

    public void flush() {
        cache.clear();
    }

    public void put(Achievement achievement) {
        cache.put(achievement.getId(), achievement);
    }
}
