package faang.school.achievement.mapper.achievement;

import faang.school.achievement.dto.achievement.AchievementProgressDto;
import faang.school.achievement.dto.achievement.UserAchievementDto;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AchievementMapper {

    @Mapping(source = "achievement.id", target = "achievementId")
    AchievementProgressDto toAchievementProgressDto(AchievementProgress achievementProgress);

    @Mapping(source = "achievement.id", target = "achievementId")
    @Mapping(source = "achievement.title", target = "achievementTitle")
    UserAchievementDto toUserAchievementDto(UserAchievement userAchievement);
}
