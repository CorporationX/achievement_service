package faang.school.achievement.repository;

import faang.school.achievement.model.AchievementProgress;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

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

    @Query(nativeQuery = true, value = """
                    INSERT INTO user_achievement_progress (user_id, achievement_id, current_points)
                    VALUES (:userId, :achievementId, 0)
                    ON CONFLICT DO NOTHING
            """)
    @Modifying
    void createProgressIfNecessary(long userId, long achievementId);

    @Query(nativeQuery = true, value = """
                    INSERT INTO user_achievement_progress (user_id, achievement_id, current_points)
                    VALUES (:userId, :achievementId, 1)
                    ON CONFLICT (user_id, achievement_id)
                    DO UPDATE SET
                        current_points = user_achievement_progress.current_points + 1,
                        updated_at = NOW(),
                        version = user_achievement_progress.version + 1
                    WHERE user_achievement_progress.current_points < :requiredPoints
            """)
    @Modifying
    int createOrIncrementAchievementProgress(long userId, long achievementId, long requiredPoints);

    List<AchievementProgress> findByUserId(long userId);
}
