package faang.school.achievement.specification.implementations;

import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.specification.interfaces.AchievementSpecificationFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleContainsFilter implements AchievementSpecificationFilter {
    @Override
    public boolean isApplicable(AchievementFilterDto filter) {
        return filter != null && filter.getTitle() != null && !filter.getTitle().isBlank();
    }

    @Override
    public Specification<Achievement> toSpecification(AchievementFilterDto filter) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("title")), "%" + filter.getTitle().toLowerCase() + "%");
    }
}
