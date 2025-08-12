package faang.school.achievement.repository;

import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.model.Achievement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementRepository extends CrudRepository<Achievement, Long> {
    default Achievement findByIdOrThrow(long id) {
        return findById(id).orElseThrow(() -> new EntityNotFoundException("Achievement not fount, id: " + id));
    }
}
