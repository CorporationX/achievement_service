package faang.school.achievement.repository;

import faang.school.achievement.model.Achievement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementRepository extends CrudRepository<Achievement, Long> {
    @Query(value = "SELECT COUNT(*) > 0 FROM achievement WHERE title = :title", nativeQuery = true)
    boolean existsByTitle(String title);
}
