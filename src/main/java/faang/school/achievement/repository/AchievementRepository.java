package faang.school.achievement.repository;

import faang.school.achievement.model.Achievement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementRepository extends CrudRepository<Achievement, Long> {

    @NonNull
    List<Achievement> findAll();
}