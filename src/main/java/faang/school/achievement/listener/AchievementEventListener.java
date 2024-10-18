package faang.school.achievement.listener;

import com.google.protobuf.InvalidProtocolBufferException;
import faang.school.achievement.dto.AchievementEvent;
import faang.school.achievement.dto.Event;
import faang.school.achievement.dto.handler.EventHandlerManager;
import faang.school.achievement.mapper.AchievementEventMapper;
import faang.school.achievement.protobuf.generate.AchievementEventProto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AchievementEventListener implements EventListener<byte[]> {

    private final EventHandlerManager<Event> handlerManager;
    private final AchievementEventMapper achievementEventMapper;

    @Override
    @KafkaListener(topics = "${spring.kafka.topics.achievement.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void onMessage(byte[] byteEvent) {
        try {
            log.info("Received achievement event");
            AchievementEventProto.AchievementEvent protoEvent = AchievementEventProto.AchievementEvent.parseFrom(byteEvent);
            AchievementEvent event = achievementEventMapper.toEvent(protoEvent);
            handlerManager.processEvent(event);
        } catch (InvalidProtocolBufferException exception) {
            log.error("An error occurred while processing achievement event", exception);
        }
    }
}
