package faang.school.achievement.cache;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.mapper.AchievementMapper;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class AchievementCacheImpl implements AchievementCache {
    private final AchievementRepository achievementRepository;
    private final AchievementMapper achievementMapper;
    private final Map<String, AchievementDto> cache = new ConcurrentHashMap<>();

    @Override
    public AchievementDto get(String title) {
        if (cache.containsKey(title)) {
            return cache.get(title);
        }
        Achievement achievement = achievementRepository.findByTitle(title).orElseThrow(
                () -> new EntityNotFoundException(String.format("Achievement with title: %s was not found", title)));
        AchievementDto achievementDto = achievementMapper.toDto(achievement);
        cache.put(title, achievementDto);

        return achievementDto;
    }
}