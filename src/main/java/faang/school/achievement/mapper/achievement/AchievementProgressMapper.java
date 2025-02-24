package faang.school.achievement.mapper.achievement;

import faang.school.achievement.dto.achievement.AchievementProgressReadDto;
import faang.school.achievement.model.AchievementProgress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AchievementProgressMapper {

    @Mapping(target = "id", source = "achievement.id")
    @Mapping(target = "title", source = "achievement.title")
    AchievementProgressReadDto toDto (AchievementProgress achievementProgress);
}
