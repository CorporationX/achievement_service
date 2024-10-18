package faang.school.achievement.service;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.exception.ResourceNotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final AchievementCache achievementCache;

    @Transactional(readOnly = true)
    public AchievementDto getByTitle(String title) {
        AchievementDto fromCache = achievementCache.getFromCache(title);
        if (fromCache != null) {
            return fromCache;
        }

        Achievement fromDb = findByTitle(title);
        achievementCache.addToCache(fromDb);

        return achievementCache.getFromCache(fromDb.getTitle());
    }

    private Achievement findByTitle(String title) {
        return achievementRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Achievement", title));
    }
}
