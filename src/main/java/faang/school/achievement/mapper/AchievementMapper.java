package faang.school.achievement.mapper;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.model.Achievement;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {AchievementProgressMapper.class})
public interface AchievementMapper {

    AchievementDto toDto(Achievement achievement);

    List<AchievementDto> toDto(List<Achievement> achievements);
}
