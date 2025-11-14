package faang.school.achievement.filter;

import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.Rarity;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class AchievementRarityFilter implements AchievementFilter {
    @Override
    public boolean isApplicable(AchievementFilterDto filterDto) {
        return filterDto.rarity() != null;
    }

    @Override
    public Stream<Achievement> apply(Stream<Achievement> achievements, AchievementFilterDto filterDto) {
        Rarity rarity = filterDto.rarity();
        return achievements.filter(achievement -> achievement.getRarity() == rarity);
    }
}