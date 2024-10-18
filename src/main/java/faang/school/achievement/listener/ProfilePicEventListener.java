package faang.school.achievement.listener;

import com.google.protobuf.InvalidProtocolBufferException;
import faang.school.achievement.dto.Event;
import faang.school.achievement.dto.ProfilePicEvent;
import faang.school.achievement.dto.handler.EventHandlerManager;
import faang.school.achievement.mapper.ProfilePicEventMapper;
import faang.school.achievement.protobuf.generate.ProfilePicEventProto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfilePicEventListener implements EventListener<byte[]> {

    private final ProfilePicEventMapper profilePicEventMapper;
    private final EventHandlerManager<Event> handlerManager;

    @Override
    @KafkaListener(topics = "${spring.kafka.topics.profile_pic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void onMessage(byte[] byteEvent) throws InvalidProtocolBufferException {
        log.info("Received profile pic byteEvent from profile pic topic");
        ProfilePicEventProto.ProfilePicEvent protoEvent = ProfilePicEventProto.ProfilePicEvent.parseFrom(byteEvent);
        ProfilePicEvent event = profilePicEventMapper.toEvent(protoEvent);
        handlerManager.processEvent(event);
    }
}
