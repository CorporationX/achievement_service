package faang.school.achievement.repository;

import faang.school.achievement.model.AchievementProgress;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AchievementProgressRepository extends CrudRepository<AchievementProgress, Long> {

    @Query(value = """
            SELECT ap
            FROM AchievementProgress ap
            WHERE ap.userId = :userId AND ap.achievement.id = :achievementId
    """)
    AchievementProgress findByUserIdAndAchievementId(long userId, long achievementId);

    @Transactional
    @Query(nativeQuery = true, value = """
            INSERT INTO user_achievement_progress (user_id, achievement_id, current_points)
            VALUES (:userId, :achievementId, 0)
            ON CONFLICT (user_id, achievement_id) DO NOTHING
    """)
    @Modifying
    void createProgressIfNecessary(
            @Param("userId") long userId,
            @Param("achievementId") long achievementId);

    @Query("SELECT ap.currentPoints FROM AchievementProgress ap " +
            "WHERE ap.userId = :userId AND ap.achievement.id = :achievementId")
    Long getCurrentPoints(@Param("userId") long userId, @Param("achievementId") long achievementId);


    @Modifying
    @Query(nativeQuery = true, value = """
            UPDATE user_achievement_progress
            SET current_points = current_points + 1,
                version = version + 1,
                updated_at = NOW()
            WHERE user_id = :userId
            AND achievement_id = :achievementId
            AND version = :version
    """)
    int incrementProgress(
            @Param("userId") long userId,
            @Param("achievementId") long achievementId,
            @Param("version") long version
    );
}
