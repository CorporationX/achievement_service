package faang.school.achievement.mapper;

import faang.school.achievement.dto.AchievementResponseDto;
import faang.school.achievement.model.Achievement;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface AchievementMapper {
    AchievementResponseDto toAchievementResponseDto(Achievement achievement);
}