package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.TeamEvent;
import faang.school.achievement.handler.manager.ManagerHandler;

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
public class TeamEventListener implements MessageListener {
    private final List<ManagerHandler> managerHandlers;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            TeamEvent event = objectMapper.readValue(message.getBody(), TeamEvent.class);
            log.info("Received team event: {}", event);
            managerHandlers.forEach(handler -> handler.startHandling(event));
        } catch (IOException e) {
            log.error("JSON processing exception " + e);
            throw new RuntimeException(e);
        }
    }
}
