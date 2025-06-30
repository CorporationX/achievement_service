package faang.school.achievement.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.model.Achievement;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AchievementDtoMapper {
    AchievementDto toDto(Achievement achievement);
    List<AchievementDto> toDtoList(List<Achievement> achievements);
}
