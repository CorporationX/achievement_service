package faang.school.achievement.mapper;

import faang.school.achievement.dto.AlbumCreatedEvent;
import faang.school.achievement.protobuf.generate.AlbumCreatedEventProto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AlbumEventMapper extends DateTimeMapper {

    AlbumCreatedEvent toAlbumCreatedEvent(AlbumCreatedEventProto.AlbumCreatedEvent protoEvent);
}
