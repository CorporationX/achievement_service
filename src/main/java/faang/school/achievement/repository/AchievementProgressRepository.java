package faang.school.achievement.repository;

import faang.school.achievement.model.AchievementProgress;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AchievementProgressRepository extends CrudRepository<AchievementProgress, Long> {

    @Query(value = """
                    SELECT ap
                    FROM AchievementProgress ap
                    WHERE ap.userId = :userId AND ap.achievement.id = :achievementId
            """)
    Optional<AchievementProgress> findByUserIdAndAchievementId(long userId, long achievementId);

    @Transactional
    @Query(nativeQuery = true, value = """
                    INSERT INTO user_achievement_progress (user_id, achievement_id, current_points)
                    VALUES (:userId, :achievementId, 0)
                    ON CONFLICT DO NOTHING
            """)
    @Modifying
    void createProgressIfNecessary(long userId, long achievementId);

    List<AchievementProgress> findByUserId(long userId);

    @Transactional
    @Query(nativeQuery = true, value = """
                    UPDATE user_achievement_progress
                    SET current_points = current_points + 1
                    WHERE id = :id
            """)
    @Modifying
    void incrementById(long id);

    @Query(nativeQuery = true, value = """
                    SELECT current_points 
                    FROM user_achievement_progress
                    WHERE id = :id
            """)
    long getPointsById(long id);
}
