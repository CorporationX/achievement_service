package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.CommentEvent;
import faang.school.achievement.event_handler.EventHandler;
import faang.school.achievement.exception.EventHandlingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CommentEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private EventHandler<CommentEvent> eventHandler;

    @Mock
    private Message message;

    private CommentEventListener eventListener;
    private byte[] validEventBytes;
    private CommentEvent validCommentEvent;

    @BeforeEach
    void setUp() {
        List<EventHandler<CommentEvent>> handlers = Collections.singletonList(eventHandler);
        eventListener = new CommentEventListener(objectMapper, handlers);
        validEventBytes = ("{\"postId\":1,\"commentId\":2, " +
                "\"authorId\":3,\"content\"::\"Goood!\"}").getBytes();
        validCommentEvent = CommentEvent.builder()
                .postId(1L)
                .commentId(2L)
                .authorId(3L)
                .content("Goood!")
                .build();
    }

    @Test
    void testOnMessage_validMessage() throws IOException {
        when(message.getBody()).thenReturn(validEventBytes);
        when(objectMapper.readValue(any(byte[].class), eq(CommentEvent.class)))
                .thenReturn(validCommentEvent);

        eventListener.onMessage(message, null);

        verify(eventHandler).handleEvent(validCommentEvent);
    }

    @Test
    void testOnMessage_throwOnDeserializationError() throws IOException {
        byte[] invalidBytes = "invalid-json".getBytes();
        when(message.getBody()).thenReturn(invalidBytes);
        when(objectMapper.readValue(any(byte[].class), eq(CommentEvent.class)))
                .thenThrow(new IOException("Deserialization error"));

        assertThrows(EventHandlingException.class, () -> eventListener.onMessage(message, null));
    }

    @Test
    void testOnMessage_emptyHandlers() throws IOException {
        when(message.getBody()).thenReturn(validEventBytes);
        when(objectMapper.readValue(any(byte[].class), eq(CommentEvent.class))).thenReturn(validCommentEvent);
        CommentEventListener emptyListener = new CommentEventListener(objectMapper, Collections.emptyList());

        emptyListener.onMessage(message, null);
    }

    @Test
    void testOnMessage_callAllHandlers() throws IOException {
        when(message.getBody()).thenReturn(validEventBytes);
        when(objectMapper.readValue(any(byte[].class), eq(CommentEvent.class))).thenReturn(validCommentEvent);
        EventHandler<CommentEvent> secondHandler = mock(EventHandler.class);
        List<EventHandler<CommentEvent>> handlers = List.of(eventHandler, secondHandler);
        CommentEventListener listener = new CommentEventListener(objectMapper, handlers);

        listener.onMessage(message, null);

        verify(eventHandler).handleEvent(validCommentEvent);
        verify(secondHandler).handleEvent(validCommentEvent);
    }

}