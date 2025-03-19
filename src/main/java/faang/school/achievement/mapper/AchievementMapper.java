package faang.school.achievement.mapper;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.UserAchievement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AchievementMapper {

    @Mapping(source = "title", target = "name")
    AchievementDto toDto(Achievement achievement);

    List<AchievementDto> toDtoList(List<Achievement> achievements);

    List<UserAchievementDto> toUserAchievementDtoList(List<UserAchievement> achievements);
}