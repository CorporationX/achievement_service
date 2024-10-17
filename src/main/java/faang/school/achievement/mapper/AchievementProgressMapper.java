package faang.school.achievement.mapper;

import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.model.AchievementProgress;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AchievementProgressMapper {
    AchievementProgressDto toDto(AchievementProgress achievementProgress);
}

