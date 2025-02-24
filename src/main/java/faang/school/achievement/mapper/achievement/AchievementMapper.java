package faang.school.achievement.mapper.achievement;

import faang.school.achievement.dto.achievement.AchievementReadDto;
import faang.school.achievement.model.Achievement;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AchievementMapper {
    AchievementReadDto toDto(Achievement achievement);

}
