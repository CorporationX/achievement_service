package faang.school.achievement.cache;

import faang.school.achievement.dto.cache.AchievementCacheDto;
import faang.school.achievement.mapper.cache.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AchievementCache {

    private final RedisTemplate<String, AchievementCacheDto> achievementRedisTemplate;
    private final AchievementRepository achievementRepository;
    private final AchievementMapper achievementMapper;

    public Achievement getByTitle(String title) {
        String key = "achievement::" + title;
        AchievementCacheDto achievementCacheDto = achievementRedisTemplate.opsForValue().get(key);
        if (achievementCacheDto != null) {
            return achievementMapper.toEntity(achievementCacheDto);
        }
        Achievement achievement = achievementRepository.findByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException("Achievement not found: " + title));

        achievementRedisTemplate.opsForValue().set(key, achievementMapper.toDto(achievement));
        return achievement;
    }
}
