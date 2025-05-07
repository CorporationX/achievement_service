package faang.school.achievement.service.cache;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.service.achievement.AchievementService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AchievementCacheInitializer {

    private final AchievementService achievementService;
    private final RedisTemplate<String, AchievementDto> redisCacheTemplate;
    private final AchievementKeyGenerator achievementKeyGenerator;

    @PostConstruct
    public void loadAllAchievementsIntoCache() {
        log.info("Starting load all achievements into cache...");
        achievementService.getAchievements().forEach(achievementDto -> {
            redisCacheTemplate.opsForValue().set(
                    achievementKeyGenerator.createAchievementKey(achievementDto.getTitle()), achievementDto
            );
        });
        log.info("Loading successfully all achievements into cache");
    }
}
