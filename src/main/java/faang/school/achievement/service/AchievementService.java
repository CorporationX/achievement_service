package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementRepository achievementRepository;

    public Achievement getAchievement(String title) {
        return achievementRepository.findAchievementByTitle(title).orElseThrow(
                () -> new EntityNotFoundException(String.format("Achievement not found by title: %s", title))
        );
    }
}
