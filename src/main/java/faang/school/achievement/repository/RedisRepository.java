package faang.school.achievement.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.achievement.AchievementDto;
import faang.school.achievement.mapper.achievement.AchievementMapper;
import faang.school.achievement.model.Achievement;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import java.util.NoSuchElementException;

@Slf4j
@Validated
@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final AchievementMapper achievementMapper;
    private final ObjectMapper objectMapper;

    public Achievement getAchievement(@NotBlank(message = "Title can't be neither blank nor null!")
                                      String achievementTitle) {

        AchievementDto achievementDto = objectMapper.convertValue(
                redisTemplate.opsForValue().get(achievementTitle), AchievementDto.class);

        if (achievementDto != null) {
            log.debug("Title {} is correct and exists in cache", achievementTitle);
            return achievementMapper.toEntity(achievementDto);
        } else {
            log.error("Such title {} does not exist!", achievementTitle);
            throw new NoSuchElementException();
        }
    }
}
