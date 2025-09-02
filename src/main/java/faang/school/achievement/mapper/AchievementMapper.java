package faang.school.achievement.mapper;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.dto.AchievementDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AchievementMapper {
    AchievementDto toAchievementDto(Achievement achievement);
}
