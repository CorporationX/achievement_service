package faang.school.achievement.mapper;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.messaging.events.AchievementEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.UserAchievement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "Spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AchievementMapper {

    @Mapping(target = "title", expression = "java(achievement.getAchievement().getTitle())")
    AchievementEvent toEvent(UserAchievement achievement);

    AchievementDto toDto(Achievement achievement);
}