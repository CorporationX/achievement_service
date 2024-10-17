package faang.school.achievement.listener.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.comment.CommentEventDto;
import faang.school.achievement.handler.comment.CommentEventHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentEventListenerTest {

    @InjectMocks
    private CommentEventListener commentEventListener;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private List<CommentEventHandler> handlers;
    private Message message;
    private CommentEventDto commentEventDto;

    @BeforeEach
    void setUp() {
        commentEventDto = CommentEventDto.builder().build();
        message = mock(Message.class);
    }

    @Test
    @DisplayName("When json object passed readValue, and pass for all handlers")
    public void whenJsonPassedThenPassItToAllHandlers() throws IOException {
        when(message.getBody()).thenReturn(new byte[0]);
        when(objectMapper.readValue(any(byte[].class), eq(CommentEventDto.class))).thenReturn(commentEventDto);
        commentEventListener.onMessage(message, null);
        verify(objectMapper).readValue(any(byte[].class), eq(CommentEventDto.class));
        verify(handlers).forEach(any());
    }

    @Test
    @DisplayName("If IOException while reading then throw exception")
    void whenIOExceptionOccursThenThrowsException() throws Exception {
        when(message.getBody()).thenReturn(new byte[0]);
        when(objectMapper.readValue(any(byte[].class), eq(CommentEventDto.class))).thenThrow(new IOException());
        assertThrows(RuntimeException.class, () -> commentEventListener.onMessage(message, null));
    }
}
