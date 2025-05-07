package faang.school.achievement.validator;

import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.properties.AchievementPageableProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AchievementPageableValidator {

    private final AchievementPageableProperties pageableProperties;

    public void validateAndSetDefaults(AchievementFilterDto filter) {
        if (filter.getPage() == null) {
            filter.setPage(pageableProperties.getDefaultPage());
        }
        if (filter.getSize() == null) {
            filter.setSize(pageableProperties.getDefaultSize());
        }
    }
}
