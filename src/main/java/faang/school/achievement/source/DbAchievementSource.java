package faang.school.achievement.source;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Component("DbAchievementSource")
public class DbAchievementSource implements AchievementSource {
    private final AchievementRepository achievementRepository;

    @Override
    public List<Achievement> getAll() {
        return StreamSupport.stream(achievementRepository.findAll().spliterator(), false).toList();
    }

    @Override
    public Optional<Achievement> getByTitle(String title) {
        Optional<Achievement> result = Optional.empty();
        List<Achievement> allAchievement = getAll();
        for (Achievement achievement : allAchievement) {
            if (achievement.getTitle().equals(title)) {
                result = Optional.of(achievement);
                break;
            }
        }
        return result;
    }

    @Override
    public Optional<Achievement> getById(long id) {
        return achievementRepository.findById(id);
    }
}