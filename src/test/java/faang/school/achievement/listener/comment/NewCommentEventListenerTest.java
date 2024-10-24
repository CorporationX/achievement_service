package faang.school.achievement.listener.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.achievement.comment.NewCommentEventDto;
import faang.school.achievement.handler.EventHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NewCommentEventListenerTest {

    @Mock
    private Message message;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private EventHandler<NewCommentEventDto> eventHandler;

    private NewCommentEventListener newCommentEventListener;
    private final Map<Class<?>, List<EventHandler<?>>> handlers = new HashMap<>();
    private NewCommentEventDto newCommentEventDto;

    @BeforeEach
    void setUp() {
        newCommentEventListener = new NewCommentEventListener(handlers, objectMapper);

        newCommentEventDto = NewCommentEventDto.builder()
                .build();
    }

    @Test
    @DisplayName("When json object passed readValue, and pass for all handlers")
    public void whenJsonPassedThenPassItToAllHandlers() throws IOException {
        handlers.put(newCommentEventDto.getClass(), List.of(eventHandler));

        when(objectMapper.readValue(message.getBody(), NewCommentEventDto.class))
                .thenReturn(newCommentEventDto);

        newCommentEventListener.onMessage(message, null);

        verify(objectMapper)
                .readValue(message.getBody(), NewCommentEventDto.class);
        verify(eventHandler)
                .handle(newCommentEventDto);
    }

    @Test
    @DisplayName("If IOException while reading then throw exception")
    void whenIOExceptionOccursThenThrowsException() throws IOException {
        when(objectMapper.readValue(message.getBody(), NewCommentEventDto.class))
                .thenThrow(new IOException());

        assertThrows(RuntimeException.class,
                () -> newCommentEventListener.onMessage(message, null));
    }

    @Test
    @DisplayName("When no handlers found then should throw exception")
    void whenListHandlersNotContainsMessageTypeThenThrowException() throws IOException {
        when(objectMapper.readValue(message.getBody(), NewCommentEventDto.class))
                .thenReturn(newCommentEventDto);

        assertThrows(RuntimeException.class,
                () -> newCommentEventListener.onMessage(message, new byte[0]));
    }
}
