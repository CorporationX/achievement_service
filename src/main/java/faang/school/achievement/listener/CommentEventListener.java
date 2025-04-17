package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.CommentEvent;
import faang.school.achievement.exception.EventConvertingException;
import faang.school.achievement.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j

public class CommentEventListener implements MessageListener {

    private final List<EventHandler<CommentEvent>> handlers;
    private final ObjectMapper mapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            CommentEvent event = mapper.readValue(message.getBody(), CommentEvent.class);
            handlers.forEach(handler -> {
                handler.handleEvent(event);
                log.debug("Team event processing on {} handler", handler.getClass().getSimpleName());
            });

        } catch (IOException e) {
            throw new EventConvertingException("Deserialized JSON %s into object %s failed",
                    message.getBody(), CommentEvent.class.getName());
        }
    }
}
