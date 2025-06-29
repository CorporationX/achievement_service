package faang.school.achievement.mapper.cache;

import faang.school.achievement.dto.cache.AchievementCacheDto;
import faang.school.achievement.model.Achievement;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AchievementMapper {

    AchievementCacheDto toDto(Achievement achievement);

    Achievement toEntity(AchievementCacheDto dto);
}
