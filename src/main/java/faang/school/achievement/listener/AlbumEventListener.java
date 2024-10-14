package faang.school.achievement.listener;

import com.google.protobuf.InvalidProtocolBufferException;
import faang.school.achievement.dto.AlbumCreatedEvent;
import faang.school.achievement.dto.Event;
import faang.school.achievement.dto.handler.EventHandlerManager;
import faang.school.achievement.mapper.AlbumEventMapper;
import faang.school.achievement.protobuf.generate.AlbumCreatedEventProto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlbumEventListener implements EventListener<byte[]> {

    private final AlbumEventMapper albumEventMapper;
    private final EventHandlerManager<Event> handlerManager;

    @Override
    @KafkaListener(topics = "${spring.kafka.topics.album-created.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void onMessage(byte[] byteEvent) throws InvalidProtocolBufferException {
        log.info("Received album created byteEvent from album created topic");
        AlbumCreatedEventProto.AlbumCreatedEvent protoEvent = AlbumCreatedEventProto.AlbumCreatedEvent.parseFrom(byteEvent);
        AlbumCreatedEvent event = albumEventMapper.toAlbumCreatedEvent(protoEvent);
        handlerManager.processEvent(event);
    }
}
