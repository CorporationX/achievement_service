package faang.school.achievement.mapper;

import faang.school.achievement.dto.AchievementProgressResponseDto;
import faang.school.achievement.model.AchievementProgress;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface AchievementProgressMapper {
    AchievementProgressResponseDto toAchievementProgressResponseDto(AchievementProgress achievementProgress);
}