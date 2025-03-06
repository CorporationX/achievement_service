package faang.school.achievement.cache;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AchievementCache {

    @Value("${achievement.key}")
    private String achievementsKey;

    private final AchievementRepository achievementRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AchievementMapper achievementMapper;

    @PostConstruct
    public void initCache() {
        refreshCache();
    }

    @Scheduled(fixedDelayString = "${achievement.cache.refresh.delay:60000}")
    public void refreshCache() {
        try {
            List<AchievementDto> achievementDtos = achievementRepository.findAll().stream()
                    .map(achievementMapper::achievementToAchievementDto)
                    .toList();

            Map<String, AchievementDto> achievementDtoMap = achievementDtos.stream()
                    .collect(Collectors.toMap(
                            AchievementDto::title,
                            Function.identity(),
                            (existing, replacement) -> existing
                    ));
            redisTemplate.opsForHash().putAll(achievementsKey, achievementDtoMap);
            log.info("Achievement cache refreshed successfully with {} items", achievementDtoMap.size());
        } catch (Exception e) {
            log.error("Error refreshing achievement cache", e);
        }
    }

    public AchievementDto getAchievement(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title must not be null or empty");
        }
        Object achievement = redisTemplate.opsForHash().get(achievementsKey, title);
        if (achievement == null) {
            log.warn("Achievement with title '{}' not found in cache", title);
        }
        return (AchievementDto) achievement;
    }
}
