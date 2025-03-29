package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.PostEvent;
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
public class PostEventListenerTest {

    private static final byte[] VALID_EVENT_BYTES = "{\"authorId\":10,\"postId\":15}".getBytes();
    private static final int AUTHOR_ID = 10;
    private static final int POST_ID = 15;
    private static final PostEvent VALID_POST_EVENT = new PostEvent(AUTHOR_ID, POST_ID);
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private EventHandler<PostEvent> eventHandler;

    @Mock
    private Message message;

    private PostEventListener eventListener;

    @BeforeEach
    void setUp() {
        List<EventHandler<PostEvent>> handlers = Collections.singletonList(eventHandler);
        eventListener = new PostEventListener(objectMapper, handlers);
    }

    @Test
    void testOnMessage_validMessage() throws IOException {
        when(message.getBody()).thenReturn(VALID_EVENT_BYTES);
        when(objectMapper.readValue(any(byte[].class), eq(PostEvent.class))).thenReturn(VALID_POST_EVENT);

        eventListener.onMessage(message, null);

        verify(eventHandler).handleEvent(VALID_POST_EVENT);
    }

    @Test
    void testOnMessage_throwOnDeserializationError() throws IOException {
        byte[] invalidBytes = "invalid-json".getBytes();
        when(message.getBody()).thenReturn(invalidBytes);
        when(objectMapper.readValue(any(byte[].class), eq(PostEvent.class)))
                .thenThrow(new IOException("Deserialization error"));

        assertThrows(EventHandlingException.class, () -> eventListener.onMessage(message, null));
    }

    @Test
    void testOnMessage_emptyHandlers() throws IOException {
        when(message.getBody()).thenReturn(VALID_EVENT_BYTES);
        when(objectMapper.readValue(any(byte[].class), eq(PostEvent.class))).thenReturn(VALID_POST_EVENT);

        PostEventListener emptyListener = new PostEventListener(objectMapper, Collections.emptyList());

        emptyListener.onMessage(message, null);
    }

    @Test
    void testOnMessage_callAllHandlers() throws IOException {
        when(message.getBody()).thenReturn(VALID_EVENT_BYTES);
        when(objectMapper.readValue(any(byte[].class), eq(PostEvent.class))).thenReturn(VALID_POST_EVENT);

        EventHandler<PostEvent> secondHandler = mock(EventHandler.class);
        List<EventHandler<PostEvent>> handlers = List.of(eventHandler, secondHandler);

        PostEventListener listener = new PostEventListener(objectMapper, handlers);

        listener.onMessage(message, null);

        verify(eventHandler).handleEvent(VALID_POST_EVENT);
        verify(secondHandler).handleEvent(VALID_POST_EVENT);
    }
}
