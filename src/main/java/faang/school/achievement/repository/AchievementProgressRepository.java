package faang.school.achievement.repository;

import faang.school.achievement.model.AchievementProgress;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AchievementProgressRepository extends CrudRepository<AchievementProgress, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = """
            SELECT ap
            FROM AchievementProgress ap
            WHERE ap.userId = :userId AND ap.achievement.id = :achievementId
    """)
    Optional<AchievementProgress> findForUpdate(long userId, long achievementId);

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
}
