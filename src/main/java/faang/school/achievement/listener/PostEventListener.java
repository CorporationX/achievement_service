package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.PostEventDto;
import faang.school.achievement.handler.EventHandler;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostEventListener extends AbstractEventListener<PostEventDto> {

    private final List<EventHandler<PostEventDto>> handlers;

    public PostEventListener(ObjectMapper objectMapper, List<EventHandler<PostEventDto>> handlers) {
        super(objectMapper);
        this.handlers = handlers;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        PostEventDto postEventDto = getEvent(message, PostEventDto.class);
        handlers.forEach(handler -> handler.handle(postEventDto.getAuthorId()));
    }
}