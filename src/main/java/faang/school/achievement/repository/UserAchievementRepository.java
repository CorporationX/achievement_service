package faang.school.achievement.repository;

import faang.school.achievement.model.AchievementTitle;
import faang.school.achievement.model.UserAchievement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAchievementRepository extends CrudRepository<UserAchievement, Long> {

    @Query("""
            SELECT COUNT(ua) > 0 FROM UserAchievement ua
            WHERE ua.userId = :userId AND ua.achievement.title = :title
            """)
    boolean hasAchievement(long userId, AchievementTitle title);

    List<UserAchievement> findByUserId(long userId);
}
