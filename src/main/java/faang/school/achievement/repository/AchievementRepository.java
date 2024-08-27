package faang.school.achievement.repository;

import faang.school.achievement.model.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    Optional<Achievement> findByTitle(String title);

    @Query("SELECT a FROM Achievement a LEFT JOIN FETCH a.userAchievements LEFT JOIN FETCH a.progresses LEFT JOIN FETCH a.rarity")
    List<Achievement> findAllWithAssociations();
}
