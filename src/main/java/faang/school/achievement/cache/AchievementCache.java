package faang.school.achievement.cache;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AchievementCache {

    private static final String ACHIEVEMENTS_KEY = "achievements";

    private final AchievementRepository achievementRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void initCache() {
        List<Achievement> achievements = achievementRepository.findAll();
        Map<String, Achievement> achievementsMap = achievements.stream()
                .collect(Collectors.toMap(Achievement::getTitle, achievement -> achievement));
        redisTemplate.opsForHash().putAll(ACHIEVEMENTS_KEY, achievementsMap);
    }

    public Achievement getAchievement(String title) {
        return (Achievement) redisTemplate.opsForHash().get(ACHIEVEMENTS_KEY, title);
    }

}
