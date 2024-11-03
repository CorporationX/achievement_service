package faang.school.achievement.config.cache;

import faang.school.achievement.dto.achievement.AchievementDto;
import faang.school.achievement.mapper.achievement.AchievementMapper;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AchievementCache {

    private final AchievementRepository achievementRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AchievementMapper achievementMapper;

    @PostConstruct
    protected void initCache() {
        achievementRepository.findAll().forEach(achievement -> {
            AchievementDto achievementDto = achievementMapper.toDto(achievement);
            redisTemplate.opsForValue().set(achievementDto.getTitle(), achievementDto);
        });
    }
}
