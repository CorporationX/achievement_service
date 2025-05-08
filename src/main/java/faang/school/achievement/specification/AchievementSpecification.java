package faang.school.achievement.specification;

import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.specification.interfaces.AchievementSpecificationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AchievementSpecification {

    private final List<AchievementSpecificationFilter> filters;

    public Specification<Achievement> filterBy(AchievementFilterDto filterDto) {
        return filters.stream()
                .filter(f -> f.isApplicable(filterDto))
                .map(f -> f.toSpecification(filterDto))
                .reduce(Specification::and)
                .orElse(null);
    }
}
