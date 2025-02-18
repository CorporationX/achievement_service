package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.EventHandler;
import faang.school.achievement.event.TaskCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final List<EventHandler> handlers;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String json = new String(message.getBody(), StandardCharsets.UTF_8);
            TaskCompletedEvent event = objectMapper.readValue(json, TaskCompletedEvent.class);
            handlers.forEach(handler -> handler.handle(event));
        } catch (Exception e) {
            log.error("Ошибка обработки события ", e);
        }
    }
}
