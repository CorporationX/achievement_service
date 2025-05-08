package faang.school.achievement.service.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.service.interfaces.AchievementRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AchievementRedisServiceImpl implements AchievementRedisService {
    private static final String KEY_MAP = "Achievements";
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Map<String, AchievementDto>> redisTemplate;

    @Override
    public void saveAchievement(Map<String, AchievementDto> achievementDto) {
        redisTemplate.opsForHash().putAll(KEY_MAP, achievementDto);
    }

    @Override
    public AchievementDto getAchievement(String title) {
        return objectMapper.convertValue(redisTemplate.opsForHash().get(KEY_MAP, title), AchievementDto.class);
    }

    @Override
    public List<AchievementDto> getAllAchievements() {
        return redisTemplate.opsForHash().values(KEY_MAP).stream()
                .map(object -> objectMapper.convertValue(object, AchievementDto.class))
                .toList();
    }

    @Override
    public void cleanAchievements() {
        Set<String> keys = redisTemplate.keys("Achievements*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    @Override
    public boolean existsByTitle(String title) {
        return redisTemplate.opsForHash().hasKey(KEY_MAP, title);
    }
}
