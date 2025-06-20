package faang.school.achievement.repository;

import faang.school.achievement.model.Achievement;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AchievementRepository extends CrudRepository<Achievement, Long> {
    Optional<Achievement> findByTitle(String title);

    @Query("SELECT a.points FROM Achievement a WHERE a.title = :title")
    Long findPointsByTitle(String title);

    @Query("SELECT a FROM Achievement a")
    @EntityGraph(value = "achievement.userAchievements", type = EntityGraph.EntityGraphType.LOAD)
    List<Achievement> findAllWithUserAchievements();

    @Query("SELECT a FROM Achievement a WHERE a.id in :ids")
    @EntityGraph(value = "achievement.progresses", type = EntityGraph.EntityGraphType.LOAD)
    List<Achievement> findAllWithProgresses(@Param("ids") List<Long> ids);
}
