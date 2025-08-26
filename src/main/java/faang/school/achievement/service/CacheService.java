package faang.school.achievement.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.AchievementDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheService {
    private final AchievementService achievementService;
    private final ObjectMapper objectMapper;

    @Cacheable(value = "achievement", key = "#title")
    public String getAchievement(String title) throws JsonProcessingException {
        AchievementDto achievementDto = achievementService.findByTitle(title);
        return objectMapper.writeValueAsString(achievementDto);
    }
}
