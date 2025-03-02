package faang.school.achievement.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;
import faang.school.achievement.event.TaskCompletedEvent;
import org.springframework.data.redis.connection.Message;


import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskEventListener implements MessageListener {
    private final ObjectMapper mapper;
    private final List<EventHandler<TaskCompletedEvent>> handlers;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String json = new String(message.getBody(), StandardCharsets.UTF_8);
            TaskCompletedEvent event = mapper.readValue(json, TaskCompletedEvent.class);
            handlers.forEach(handler -> handler.handle(event));
        } catch (Exception e) {
            log.error("Ошибка обработки события ", e);
        }
    }
}
