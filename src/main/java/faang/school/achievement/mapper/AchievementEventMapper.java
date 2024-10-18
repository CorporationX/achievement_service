package faang.school.achievement.mapper;

import faang.school.achievement.dto.AchievementEvent;
import faang.school.achievement.protobuf.generate.AchievementEventProto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AchievementEventMapper extends DateTimeMapper {

    AchievementEvent toEvent(AchievementEventProto.AchievementEvent proto);

    AchievementEventProto.AchievementEvent toProto(AchievementEvent event);
}
