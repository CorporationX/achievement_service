package faang.school.achievement.mapper;

import faang.school.achievement.dto.ProfilePicEvent;
import faang.school.achievement.protobuf.generate.ProfilePicEventProto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfilePicEventMapper extends DateTimeMapper {

    ProfilePicEvent toEvent(ProfilePicEventProto.ProfilePicEvent proto);
}
