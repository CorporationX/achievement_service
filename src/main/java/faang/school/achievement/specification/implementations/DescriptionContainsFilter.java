package faang.school.achievement.specification.implementations;

import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.specification.interfaces.AchievementSpecificationFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class DescriptionContainsFilter implements AchievementSpecificationFilter {

    @Override
    public boolean isApplicable(AchievementFilterDto filter) {
        return filter != null && filter.getDescription() != null && !filter.getDescription().isBlank();
    }

    @Override
    public Specification<Achievement> toSpecification(AchievementFilterDto filter) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("description")), "%" + filter.getDescription().toLowerCase() + "%");
    }
}
