package faang.school.achievement.repository;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.EventType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementRepository extends CrudRepository<Achievement, Long> {

    @Query("SELECT a FROM Achievement a WHERE a.event = :event")
    List<Achievement> findByEvent(@Param("event") EventType event);
}
