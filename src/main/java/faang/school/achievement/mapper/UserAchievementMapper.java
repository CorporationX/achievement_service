package faang.school.achievement.mapper;

import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.model.UserAchievement;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserAchievementMapper {

    UserAchievementDto toDto(UserAchievement userAchievement);
}
