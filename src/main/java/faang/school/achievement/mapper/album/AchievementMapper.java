package faang.school.achievement.mapper.album;

import faang.school.achievement.dto.album.AlbumCreatedEvent;
import faang.school.achievement.model.Achievement;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AchievementMapper {

    Achievement toEntity(AlbumCreatedEvent event);
}
