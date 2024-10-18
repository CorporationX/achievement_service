package faang.school.achievement.mapper;

import faang.school.achievement.dto.AchievementEvent;
import faang.school.achievement.model.Achievement;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AchievementEventMapper {

    AchievementEvent toDto(Achievement achievement);

}
