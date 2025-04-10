package faang.school.achievement.repository.adapter;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AchievementRepositoryAdapter {
    private final AchievementRepository achievementRepository;

    public Achievement getByTitle(String title) {
        return achievementRepository.findByTitle(title).orElseThrow(
                () -> new EntityNotFoundException(String.format("Achievement with title %s not found", title)));
    }
}
