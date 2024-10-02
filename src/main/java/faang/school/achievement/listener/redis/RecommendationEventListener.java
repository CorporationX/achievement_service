package faang.school.achievement.listener.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.handler.RecommendationEventHandler;
import faang.school.achievement.redis.event.RecommendationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class RecommendationEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final List<RecommendationEventHandler> handlers;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        String body = new String(message.getBody());
        log.info("Received message from channel {}: {}", channel, body);

        RecommendationEvent recommendationEvent;
        try {
            recommendationEvent = objectMapper.readValue(body, RecommendationEvent.class);
        } catch (JsonProcessingException e) {
            log.error("There was an exception during conversion String to CommentEvent.class");
            throw new RuntimeException(e);
        }
        handlers.forEach(recommendationEventHandler -> recommendationEventHandler.handleEvent(recommendationEvent));
    }
}
