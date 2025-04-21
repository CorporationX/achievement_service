package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.error.LikeEvent;
import faang.school.achievement.exception.EventConvertingException;
import faang.school.achievement.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LikeEventListener implements MessageListener {

    private final ObjectMapper mapper;
    private final List<EventHandler<LikeEvent>> likeHandlers;


    @Override
    public void onMessage(Message message, @Nullable byte[] pattern) {
        try {
            LikeEvent event = mapper.readValue(message.getBody(), LikeEvent.class);
            likeHandlers.forEach(
                    handler -> {
                        handler.handleEvent(event);
                    });
        } catch (IOException e) {
            throw new EventConvertingException("Ошибка чтения JSON: {}", LikeEvent.class);
        }

    }

}
