package faang.school.achievement.repository;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.Rarity;
import org.springframework.data.jpa.repository.Query;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface AchievementRepository extends CrudRepository<Achievement, Long> {

    @Query("SELECT a FROM Achievement a WHERE title LIKE %:title% OR description LIKE %:description% OR rarity = :rarity")
    public List<Achievement> findAchievementByFilters(@Param("title") String title,
                                                      @Param("description") String description,
                                                      @Param("rarity") Rarity rarity);

    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    Optional<Achievement> findByTitle(String title);
}
