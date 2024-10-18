package faang.school.achievement.cache;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
public class AchievementCache extends AbstractCache<AchievementDto> {
    private static final String PREFIX = "achievement";
    private static final Duration DURATION = Duration.ofHours(1);

    private final AchievementRepository achievementRepository;
    private final AchievementMapper achievementMapper;

    public AchievementCache(RedisTemplate<String, AchievementDto> achievementRedisTemplate,
                            AchievementRepository achievementRepository, AchievementMapper achievementMapper) {
        super(achievementRedisTemplate, PREFIX, DURATION);
        this.achievementRepository = achievementRepository;
        this.achievementMapper = achievementMapper;
    }

    @PostConstruct
    public void fillAchievements() {
        List<Achievement> achievements = achievementRepository.findAll();
        addToCache(achievements);
    }

    public void addToCache(List<Achievement> achievements) {
        achievements.forEach(this::addToCache);
    }

    public void addToCache(Achievement achievement) {
        AchievementDto dto = achievementMapper.toDto(achievement);
        set(key(dto.getTitle()), dto);
    }

    @Async
    public void addToCache(AchievementDto dto) {
        set(key(dto.getTitle()), dto);
    }

    public AchievementDto getFromCache(String title) {
        return get(key(title));
    }
}
