package faang.school.achievement.mapper;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.Rarity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AchievementMapper {

    default String map(Rarity rarity) {
        return rarity == null ? null : rarity.name();
    }

    default AchievementDto toDto(Achievement achievement) {
        if (achievement == null) {
            return null;
        }
        return new AchievementDto(
                achievement.getId(),
                achievement.getTitle(),
                achievement.getDescription(),
                map(achievement.getRarity()),
                achievement.getPoints()
        );
    }
}
