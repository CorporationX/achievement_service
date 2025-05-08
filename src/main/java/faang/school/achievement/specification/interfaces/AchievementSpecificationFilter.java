package faang.school.achievement.specification.interfaces;

import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.model.Achievement;
import org.springframework.data.jpa.domain.Specification;

public interface AchievementSpecificationFilter {

    boolean isApplicable(AchievementFilterDto filter);

    Specification<Achievement> toSpecification(AchievementFilterDto filter);
}
