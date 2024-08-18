package faang.school.achievement.service.filter;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.model.Rarity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Stream;

@Component
public class RarelyFilter implements AchievementFilter {

    @Override
    public boolean isApplicable(AchievementFilterDto filter) {
        return filter.getRarityPattern() != null && !filter.getRarityPattern().isEmpty()
                && Arrays.stream(Rarity.values()).anyMatch(rarity -> rarity.name().contains(filter.getRarityPattern()));
    }

    @Override
    public Stream<AchievementDto> apply(Stream<AchievementDto> achievementStream, AchievementFilterDto filter) {
        return achievementStream.filter(achievement -> achievement.getRarity().name().toLowerCase()
                .contains(filter.getRarityPattern().toLowerCase()));
    }
}
