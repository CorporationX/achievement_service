package faang.school.achievement.service;

import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementRepository achievementRepository;

    @Cacheable(value = "achievementTitle")
    public Achievement getByTitle(String title) {
        return achievementRepository.findByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException("Достижения с названием %s не существует"
                        .formatted(title)));
    }

}
