package faang.school.achievement.listener.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.config.kafka.KafkaProperties;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.messaging.events.GoalAttachedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoalEventListener {
    private final ObjectMapper objectMapper;
    private final List<EventHandler<GoalAttachedEvent>> handlers;
    private final KafkaProperties properties;

    @KafkaListener(topics = "${spring.data.kafka.topics.goal-attached}")
    public void listen(String json) {
        if (!properties.isUseKafka()) return;
        //todo in 77690
    }
}