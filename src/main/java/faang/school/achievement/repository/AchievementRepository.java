package faang.school.achievement.repository;

import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.model.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    default Achievement findByIdOrThrow(long id) {
        return findById(id).orElseThrow(() -> new EntityNotFoundException("Achievement not fount, id: " + id));
    }

    Optional<Achievement> findByTitle(String title);

    default Achievement findByTitleOrThrow(String title) {
        return findByTitle(title).orElseThrow(() -> new EntityNotFoundException("Achievement not fount, title: " + title));
    }
}
