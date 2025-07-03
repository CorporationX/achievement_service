package faang.school.achievement.listener.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoalEventListener{
    private final ObjectMapper objectMapper;
    private final List<EventHandler> handlers;
    @Value("${spring.data.kafka.use-kafka}")
    private boolean useKafka;

    @KafkaListener(topics = "goal_attached")
    public void listen(String json) {
        if(!useKafka) return;
    }
}