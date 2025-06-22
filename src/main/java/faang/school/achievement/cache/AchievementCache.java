package faang.school.achievement.cache;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AchievementCache {
    private static final String CACHE_KEY_PREFIX = "achievement:";
    private final AchievementRepository achievementRepository;
    private RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    private void initCache() {
        for (Achievement achievement : achievementRepository.findAll()) {
            redisTemplate.opsForValue().set(CACHE_KEY_PREFIX + achievement.getTitle(), achievement);
        }
    }

    public Optional<Achievement> get(String title) {
        String redisKey = CACHE_KEY_PREFIX + title;
        Achievement achievement = (Achievement) redisTemplate.opsForValue().get(redisKey);

        if (achievement != null ) return Optional.of(achievement);
        return Optional.empty();
    }
}
