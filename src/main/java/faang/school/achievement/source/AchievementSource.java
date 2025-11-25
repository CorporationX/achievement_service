package faang.school.achievement.source;

import faang.school.achievement.model.Achievement;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.Optional;

public interface AchievementSource {
    List<Achievement> getAll();

    Optional<Achievement> getByTitle(@NotBlank String title);

    Optional<Achievement> getById(@Positive long id);
}
