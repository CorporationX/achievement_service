package faang.school.achievement.repository;

import faang.school.achievement.model.Achievement;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AchievementRepository extends CrudRepository<Achievement, Long> {

    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    Optional<Achievement> findByTitle(String title);
}
