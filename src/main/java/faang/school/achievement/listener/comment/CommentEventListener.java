package faang.school.achievement.listener.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.comment.CommentEventDto;
import faang.school.achievement.handler.comment.CommentEventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentEventListener implements MessageListener {

    private final List<CommentEventHandler> handlers;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            CommentEventDto commentEventDto = objectMapper.readValue(message.getBody(),
                    CommentEventDto.class);
            //todo связать со списком handlers и определить по дто в какие хэндлеры отправлять эти события
            //todo логику кэша, и дальнейшей отправке в EvilCommenterHandler
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
