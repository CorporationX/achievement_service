package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.InviteSentEventDto;
import faang.school.achievement.handler.AbstractAchievementHandler;
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
public class InviteEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final List<AbstractAchievementHandler<InviteSentEventDto>> handlers;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("InviteEventListener has received a new message from Redis");
        InviteSentEventDto event;
        try {
            event = objectMapper.readValue(message.getBody(), InviteSentEventDto.class);
            handlers.forEach(handler -> handler.handle(event));
        } catch (IOException e) {
            log.error("IOException while parsing message in InviteEventListener...");
            throw new RuntimeException(e);
        }
    }
}
