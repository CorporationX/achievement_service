package faang.school.achievement.repository;

import faang.school.achievement.model.Achievement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AchievementRepository extends CrudRepository<Achievement, Long> {
    @Query(nativeQuery = true, value = """
            SELECT * FROM achievement
            WHERE UPPER(title) = UPPER(:title)
            ORDER BY updated_at desc
            LIMIT 1
            """)
    Optional<Achievement> findByTitle(String title);
}
