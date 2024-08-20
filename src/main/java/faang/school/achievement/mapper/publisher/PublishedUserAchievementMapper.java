package faang.school.achievement.mapper.publisher;

import faang.school.achievement.dto.PublishedUserAchievementDto;
import faang.school.achievement.model.UserAchievement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PublishedUserAchievementMapper {

    @Mapping(source = "achievement.id", target = "achievementId")
    @Mapping(source = "achievement.title", target = "title")
    PublishedUserAchievementDto toDto(UserAchievement userAchievement);

    @Mapping(source = "achievementId", target = "achievement.id")
    @Mapping(source = "title", target = "achievement.title")
    UserAchievement toEntity(PublishedUserAchievementDto publishedUserAchievementDto);
}
