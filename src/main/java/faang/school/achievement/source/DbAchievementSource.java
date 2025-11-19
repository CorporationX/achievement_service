package faang.school.achievement.source;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Component("DbAchievementSource")
@Validated
@Slf4j
public class DbAchievementSource implements AchievementSource {
    private final AchievementRepository achievementRepository;

    @Override
    public List<Achievement> getAll() {
        log.debug("All achievements are taken from the database");
        return StreamSupport.stream(achievementRepository.findAll().spliterator(), false).toList();
    }

    @Override
    public Optional<Achievement> getByTitle(@NotBlank String title) {
        log.debug("Achievement with title = {} taken from the database", title);
        return achievementRepository.findByTitle(title);
    }

    @Override
    public Optional<Achievement> getById(@Positive long id) {
        log.debug("Achievement with id = {} taken from the database", id);
        return achievementRepository.findById(id);
    }
}