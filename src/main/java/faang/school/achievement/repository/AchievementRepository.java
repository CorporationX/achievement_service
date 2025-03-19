package faang.school.achievement.repository;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.Rarity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    @Query("SELECT a FROM Achievement a WHERE " +
    "(:name IS NULL OR a.title LIKE %:name%) AND " +
    "(:description IS NULL OR a.description LIKE %:description%) AND " +
    "(:rarity IS NULL OR a.rarity = :rarity)")
    List<Achievement> findByFilters(String name, String description, Rarity rarity);
}