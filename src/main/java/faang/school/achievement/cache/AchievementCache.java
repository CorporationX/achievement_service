package faang.school.achievement.cache;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class AchievementCache {
    private static final String CACHE_KEY_PREFIX = "achievement:";
    private final AchievementRepository achievementRepository;
    private final Map<String, AchievementDto> cache = new ConcurrentHashMap<>();
    private final AchievementMapper achievementMapper;

    @PostConstruct
    @Transactional
    private void init() {
        for (Achievement achievement : achievementRepository.findAll()) {
            AchievementDto achievementDto = achievementMapper.toDto(achievement);
            cache.put(CACHE_KEY_PREFIX + achievement.getTitle(), achievementDto);
        }
    }

    public Optional<AchievementDto> get(String title) {
        String redisKey = CACHE_KEY_PREFIX + title;
        AchievementDto achievementDto = cache.get(redisKey);

        if (achievementDto != null) return Optional.of(achievementDto);
        return Optional.empty();
    }
}