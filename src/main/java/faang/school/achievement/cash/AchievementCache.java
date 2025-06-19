package faang.school.achievement.cash;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AchievementCache {

    private final AchievementRepository achievementRepository;

    @Cacheable(value = "achievements", key = "#title")
    public Achievement getByTitle(String title) {
        return achievementRepository.findByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException("Achievement not found: " + title));
    }
}
