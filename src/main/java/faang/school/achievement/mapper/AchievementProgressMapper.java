package faang.school.achievement.mapper;

import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.model.AchievementProgress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AchievementProgressMapper {

    @Mapping(target = "achievementId", source = "achievement.id")
    UserAchievementDto toDto(AchievementProgress achievementProgress);

    List<UserAchievementDto> toDtos(List<AchievementProgress> achievementProgressList);
}
