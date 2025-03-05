package faang.school.achievement.mapper;

import faang.school.achievement.dto.event.ProfilePicEvent;
import faang.school.achievement.redis.event.ProfilePicRedisEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(target = "profilePicUrl", source = "event.picKey")
    @Mapping(target = "source", source = "source")
    ProfilePicEvent toProfilePicEvent(ProfilePicRedisEvent event, Object source);
}
