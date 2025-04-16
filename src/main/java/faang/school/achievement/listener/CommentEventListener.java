package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.CommentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor

public class CommentEventListener implements MessageListener {

    private final ObjectMapper mapper;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            CommentEvent event = mapper.readValue(message.getBody(), CommentEvent.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
