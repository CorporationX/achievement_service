package faang.school.achievement.mapper;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.model.Achievement;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AchievementMapper {
    AchievementMapper INSTANCE = Mappers.getMapper(AchievementMapper.class);

    AchievementDto achievementToAchievementDTO(Achievement achievement);
}
