package faang.school.achievement.listener;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import faang.school.achievement.dto.event.PostCreateEventDto;
import faang.school.achievement.handler.EventHandler;

@Component
public class PostCreateEventListener extends RedisEventListener<PostCreateEventDto> {
    public PostCreateEventListener(
        RedisTemplate<String, Object> redisTemplate, 
        ObjectMapper objectMapper,
        @Value("${spring.data.redis.channels.postCreated}")String topicName,
        List<EventHandler<PostCreateEventDto>> handlers
    ) {
        super(redisTemplate, objectMapper, PostCreateEventDto.class, topicName, handlers);
    }

    @Override
    protected void handleEvent(PostCreateEventDto event) {
        for (EventHandler<PostCreateEventDto> handler : handlers) {
            handler.handle(event);
        }
    }
}
