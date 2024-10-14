package faang.school.achievement.mapper;

import faang.school.achievement.dto.FollowerEvent;
import faang.school.achievement.protobuf.generate.FollowerEventProto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FollowerEventMapper extends DateTimeMapper {

    FollowerEvent toFollowerEvent(FollowerEventProto.FollowerEvent protoEvent);
}
