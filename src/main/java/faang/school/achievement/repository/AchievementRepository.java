package faang.school.achievement.repository;

import faang.school.achievement.model.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {
   Achievement findByTitle(String title);

    @Query("""
            SELECT a FROM Achievement a
            LEFT JOIN FETCH AchievementProgress ap
            WHERE NOT EXISTS (
                SELECT ua FROM UserAchievement ua
                WHERE ua.achievement.id = a.id AND ua.userId = :userId
            )
            """)
    List<Achievement> findUnobtainedAchievementsWithProgressByUserId(long userId);
}
