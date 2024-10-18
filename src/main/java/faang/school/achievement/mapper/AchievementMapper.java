package faang.school.achievement.mapper;

import faang.school.achievement.dto.achievement.UserAchievementEventDto;
import faang.school.achievement.model.UserAchievement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AchievementMapper {

    @Mapping(source = "achievement.title", target = "title")
    @Mapping(source = "achievement.rarity", target = "rarity")
    @Mapping(source ="achievement.id",target = "achievementId")
    UserAchievementEventDto toUserAchievementEventDto(UserAchievement userAchievement);
}
