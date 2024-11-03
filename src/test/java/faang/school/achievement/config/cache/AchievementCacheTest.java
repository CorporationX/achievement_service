package faang.school.achievement.config.cache;

import faang.school.achievement.dto.achievement.AchievementDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.util.BaseContextTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AchievementCacheTest extends BaseContextTest {

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    @DisplayName("When context loads then redis cache should contains all achievements from db")
    public void whenContextLoadsThenRedisCacheShouldContainsAllAchievementsFromDb() {
        List<Achievement> achievements = achievementRepository.findAll();

        achievements.forEach(achievement -> {
            Object object = redisTemplate.opsForValue().get(achievement.getTitle());
            AchievementDto achievementDto = objectMapper.convertValue(object, AchievementDto.class);

            assertNotNull(achievementDto);
            assertEquals(achievement.getId(), achievementDto.getId());
            assertEquals(achievement.getTitle(), achievementDto.getTitle());
            assertEquals(achievement.getDescription(), achievementDto.getDescription());
            assertEquals(achievement.getRarity(), achievementDto.getRarity());
            assertEquals(achievement.getPoints(), achievementDto.getPoints());
        });
    }
}
