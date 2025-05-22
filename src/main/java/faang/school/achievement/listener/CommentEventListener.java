package faang.school.achievement.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.event.CommentEvent;
import faang.school.achievement.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final List<EventHandler<CommentEvent>> handlers;

    @Override
    public void onMessage(Message message, byte[] pattern) {

        try {
            String json = new String(message.getBody(), StandardCharsets.UTF_8);
            CommentEvent event = objectMapper.readValue(json, CommentEvent.class);

            for (EventHandler<CommentEvent> handler : handlers) {
                handler.handle(event);
            }

        } catch (Exception e) {
            log.error("❌ Ошибка при обработке Redis-сообщения из comment_channel", e);
        }
    }
}
