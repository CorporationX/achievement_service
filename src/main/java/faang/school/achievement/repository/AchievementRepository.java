package faang.school.achievement.repository;

import faang.school.achievement.model.Achievement;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    @Query(value = """
            FROM Achievement a
            """)
    @EntityGraph(attributePaths = {"userAchievements", "progresses"})
    Set<Achievement> findAllWithLazyCollections();
}
