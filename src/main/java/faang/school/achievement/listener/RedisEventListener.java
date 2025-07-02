package faang.school.achievement.listener;

import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import faang.school.achievement.handler.EventHandler;

import java.util.List;

import org.springframework.data.redis.connection.Message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Getter
@Component
public abstract class RedisEventListener<T> implements MessageListener {
    protected RedisTemplate<String, Object> redisTemplate;
    protected ObjectMapper objectMapper;
    protected Class<T> eventType;
    protected String topicName;
    protected List<EventHandler<T>> handlers;


    @Override
    public void onMessage(Message message, @SuppressWarnings("null") byte[] pattern) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventType);
            handleEvent(event);
            log.info("Received {} event: {}", eventType.getSimpleName(), event);
        } catch (Exception e) {
            log.error("Failed to process dto.", e);
        }
    }

    protected abstract void handleEvent(T event);

    public ChannelTopic getTopic() {
        return new ChannelTopic(topicName);
    }
}
