package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.PostEvent;
import faang.school.achievement.dto.handler2.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class PostEventListener implements MessageListener {
    private final List<EventHandler<PostEvent>> handlers;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        PostEvent postEvent;
        try {
            postEvent = objectMapper.readValue(message.getBody(), PostEvent.class);
        } catch (IOException e) {
            log.error("cannot get PostEvent from message {}", message, e);
            throw new RuntimeException(e);
        }
        handlers.forEach(handler -> handler.handle(postEvent));
    }
}
