package faang.school.achievement.redis;

import faang.school.achievement.exceptions.ObjectNotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementCode;
import faang.school.achievement.repository.AchievementRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class AchievementCache {
    private final RedisTemplate<String, Achievement> redisStringTemplate;
    private final AchievementKeyBuilder achievementKeyBuilder;
    private final AchievementRepository achievementRepository;

    @Value("${spring.data.redis.achievements.ttl-s}")
    private long achievementKeyTTL;

    public AchievementCache(@Qualifier("redisAchievementTemplate") RedisTemplate<String, Achievement> redisStringTemplate,
                            AchievementKeyBuilder achievementKeyBuilder,
                            AchievementRepository achievementRepository) {
        this.redisStringTemplate = redisStringTemplate;
        this.achievementKeyBuilder = achievementKeyBuilder;
        this.achievementRepository = achievementRepository;
    }

    @Transactional
    public void init() {
        log.info("Achievement Cache init");
        List<Achievement> achievements = getAchievements();
        log.info("Loaded achievements list size: {}", achievements.size());

        redisStringTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) {
                RedisOperations<String, Achievement> ops = operations;
                for (Achievement achievement : achievements) {
                    String key = achievementKeyBuilder.achievementKey(achievement.getTitle());
                    ops.opsForValue().set(key, achievement, achievementKeyTTL, TimeUnit.SECONDS);
                }
                return null;
            }
        });
    }

    public List<Achievement> getAchievements() {
        List<Achievement> allWithUserAchievements = achievementRepository.findAllWithUserAchievements();
        List<Long> ids = allWithUserAchievements.stream()
                .map(Achievement::getId)
                .toList();
        return achievementRepository.findAllWithProgresses(ids);
    }

    public Achievement getAchievementByCode(AchievementCode achievementCode) {
        Achievement cachedAchievement = redisStringTemplate.opsForValue()
                .get(achievementKeyBuilder.achievementKey(achievementCode.getName()));
        if (cachedAchievement == null) {
            Achievement loadedAchievement = loadAchievementByTitle(achievementCode.getName());
            addToCache(loadedAchievement);
            return loadedAchievement;
        }
        return cachedAchievement;
    }

    public Achievement loadAchievementByTitle(String title) {
        return achievementRepository.findByTitle(title).orElseThrow(() ->
                new ObjectNotFoundException(String.format("Achievement with title %s not found", title)));
    }

    private void addToCache(Achievement loadedAchievement) {
        redisStringTemplate.opsForValue().set(achievementKeyBuilder.achievementKey(loadedAchievement.getTitle()),
                loadedAchievement, achievementKeyTTL, TimeUnit.SECONDS);
    }
}