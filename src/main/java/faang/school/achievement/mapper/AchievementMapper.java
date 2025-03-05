package faang.school.achievement.mapper;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.model.Achievement;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "Spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AchievementMapper {
    AchievementDto toAchievementDto(Achievement achievement);

    Achievement toAchievement(AchievementDto achievementDto);
}
