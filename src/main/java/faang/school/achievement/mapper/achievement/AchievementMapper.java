package faang.school.achievement.mapper.achievement;

import faang.school.achievement.dto.achievement.AchievementDto;
import faang.school.achievement.dto.achievement.AchievementProgressDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AchievementMapper {

    @Mapping(source = "achievement.id", target = "achievementId")
    AchievementProgressDto toAchievementProgressDto(AchievementProgress achievementProgress);

    Achievement toEntity(AchievementDto achievementDto);

    AchievementDto toDto(Achievement achievement);
}
