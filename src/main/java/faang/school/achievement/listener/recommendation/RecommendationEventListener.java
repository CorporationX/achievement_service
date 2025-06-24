package faang.school.achievement.listener.recommendation;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.config.redis.RedisProperties;
import faang.school.achievement.events.RecommendationEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.listener.AbstractEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecommendationEventListener extends AbstractEventListener {
    private final List<String> topicNameKeys = List.of("recommendation-event");
    private final RedisProperties redisProperties;
    private final ObjectMapper objectMapper;
    private final List<EventHandler> handlers;

    @Override
    public Set<ChannelTopic> getChannelTopics() {
        return super.getChannelTopics(topicNameKeys, redisProperties);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            RecommendationEvent recommendationEvent = objectMapper.readValue(
                    message.getBody(),
                    RecommendationEvent.class
            );
            handlers.forEach(handler -> handler.handle(recommendationEvent));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
