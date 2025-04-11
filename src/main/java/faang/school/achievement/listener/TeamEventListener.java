package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.TeamEvent;
import faang.school.achievement.exception.EventConvertingException;
import faang.school.achievement.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class TeamEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final List<EventHandler<TeamEvent>> teamHandlers;

    @Override
    public void onMessage(Message message, @Nullable byte[] pattern) {
        try {
            TeamEvent teamEvent = objectMapper.readValue(message.getBody(), TeamEvent.class);
            teamHandlers.forEach(handler -> {
                handler.handleEvent(teamEvent);
                log.debug("Team event processing on {} handler", handler.getClass().getSimpleName());
            });
        } catch (IOException e) {
            throw new EventConvertingException("Deserialized JSON %s into object %s failed",
                    message.getBody(), TeamEvent.class.getName());
        }
    }
}
