package faang.school.achievement.cache;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.mapper.AchievementMapper;
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
        List<AchievementDto> achievementDTOs = achievementRepository.findAll().stream()
                .map(AchievementMapper.INSTANCE::achievementToAchievementDTO)
                .toList();

        Map<String, AchievementDto> achievementsMap = achievementDTOs.stream()
                .collect(Collectors.toMap(AchievementDto::title, dto -> dto));

        redisTemplate.opsForHash().putAll(ACHIEVEMENTS_KEY, achievementsMap);
    }

    public AchievementDto getAchievement(String title) {
        return (AchievementDto) redisTemplate.opsForHash().get(ACHIEVEMENTS_KEY, title);
    }
}
