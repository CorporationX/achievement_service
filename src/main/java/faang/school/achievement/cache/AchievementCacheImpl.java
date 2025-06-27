package faang.school.achievement.cache;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class AchievementCacheImpl implements AchievementCache {
    private final AchievementRepository achievementRepository;
    private final Map<String, AchievementDto> cache = new ConcurrentHashMap<>();
    private final AchievementMapper achievementMapper;

    @PostConstruct
    private void init() {
        for (Achievement achievement : achievementRepository.findAll()) {
            AchievementDto achievementDto = achievementMapper.toDto(achievement);
            cache.put(achievement.getTitle(), achievementDto);
        }
    }

    @Override
    public Optional<AchievementDto> get(String title) {
        return Optional.ofNullable(cache.get(title));
    }
}