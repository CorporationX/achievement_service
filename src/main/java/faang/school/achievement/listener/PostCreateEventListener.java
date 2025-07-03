package faang.school.achievement.listener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import faang.school.achievement.dto.event.PostCreateEventDto;
import faang.school.achievement.handler.EventHandler;

@Component
public class PostCreateEventListener extends RedisEventListener<PostCreateEventDto> {
    public PostCreateEventListener(@Value("${spring.data.redis.channels.postCreated}") String topicName) {
        super(PostCreateEventDto.class, topicName);
    }        

    @Override
    protected void handleEvent(PostCreateEventDto event) {
        for (EventHandler<PostCreateEventDto> handler : getHandlers()) {
            handler.handle(event);
        }
    }
}
