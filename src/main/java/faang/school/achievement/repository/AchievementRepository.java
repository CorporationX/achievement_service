package faang.school.achievement.repository;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.Rarity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    Optional<Achievement> findByTitle(String title);
    void deleteByTitle(String title);

    @Query("SELECT a FROM Achievement a WHERE " +
            "(:title IS NULL OR a.title = :title) AND " +
            "(:description IS NULL OR a.description LIKE %:description%) AND " +
            "(:rarity IS NULL OR a.rarity = :rarity)")
    List<Achievement> findFilteredAchievements(String title, String description, Rarity rarity, Pageable pageable);
}
