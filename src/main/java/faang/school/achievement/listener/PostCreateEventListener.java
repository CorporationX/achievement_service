package faang.school.achievement.listener;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import faang.school.achievement.config.redis.RedisMessageListener;
import faang.school.achievement.dto.event.PostCreateEventDto;
import faang.school.achievement.handler.post.PostCreateEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostCreateEventListener implements RedisMessageListener {
    private final List<PostCreateEventHandler> handlers;
    private final ObjectMapper objectMapper;
    @Value("${spring.data.redis.channels.postCreated}")
    private String topicName;
    
    @SuppressWarnings("null")
    @Override
    public void onMessage(Message message, byte[] pattern) {
        PostCreateEventDto postCreateEventDto;
        try {
            postCreateEventDto = objectMapper.readValue(message.getBody(), PostCreateEventDto.class);
            for (PostCreateEventHandler handler : handlers) {
                handler.handle(postCreateEventDto);
            }
        } catch (IOException e) {
            log.error("Error while converting from message to dto object, {}", e.getMessage());
            throw new RuntimeException("Error while converting from message to dto object", e);
        }
    }

    @Override
    public ChannelTopic getTopic() {
        return new ChannelTopic(topicName);
    }
}
