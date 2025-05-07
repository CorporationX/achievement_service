package faang.school.achievement.mapper;

import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.model.AchievementProgress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AchievementProgressMapper {

    @Mapping(source = "achievement.id", target = "id")
    @Mapping(source = "achievement.title", target = "title")
    @Mapping(source = "achievement.description", target = "description")
    @Mapping(source = "achievement.rarity", target = "rarity")
    @Mapping(source = "achievement.points", target = "totalPoints")
    AchievementProgressDto toDto(AchievementProgress progress);

    @Mapping(source = "achievement.id", target = "id")
    @Mapping(source = "achievement.title", target = "title")
    @Mapping(source = "achievement.description", target = "description")
    @Mapping(source = "achievement.rarity", target = "rarity")
    @Mapping(source = "achievement.points", target = "totalPoints")
    List<AchievementProgressDto> toDtoList(List<AchievementProgress> progressList);
}
