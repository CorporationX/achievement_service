package faang.school.achievement.mapper;

import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.model.AchievementProgress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AchievementProgressMapper {

    @Mapping(source = "achievement", target = "achievement")
    AchievementProgressDto toDto(AchievementProgress achievementProgress);
}
