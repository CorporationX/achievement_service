package faang.school.achievement.mapper;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.model.Achievement;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AchievementMapper {

    AchievementDto toDto(Achievement achievement);

    List<AchievementDto> toDtoList(Iterable<Achievement> achievements);
}
