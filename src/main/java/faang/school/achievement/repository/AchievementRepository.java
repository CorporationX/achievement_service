package faang.school.achievement.repository;

import faang.school.achievement.model.Achievement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AchievementRepository extends CrudRepository<Achievement, Long> {
    @Query("select a from Achievement a where upper(a.title) = upper(?1)")
    Optional<Achievement> findByTitleIgnoreCase(String title);


}
