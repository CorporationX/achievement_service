package faang.school.achievement.listener.followerEvent;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.FollowerEvent;
import faang.school.achievement.service.eventHandler.FollowerEventHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class FollowerEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final List<FollowerEventHandler> followerEventHandlers;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        String body = new String(message.getBody());
        log.info("Received message from channel " + channel + ": " + body);

        try {
            FollowerEvent event = objectMapper.readValue(body, FollowerEvent.class);
            followerEventHandlers.forEach(handler -> handler.handle(event));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
