package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.InviteSentEvent;
import faang.school.achievement.event_handler.EventHandler;
import faang.school.achievement.exception.EventHandlingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InviteSentEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private EventHandler<InviteSentEvent> eventHandler;

    @Mock
    private EventHandler<InviteSentEvent> handler;

    @Mock
    private Message message;

    @InjectMocks
    private InviteSentEventListener eventListener;

    @BeforeEach
    void setUp() {
        List<EventHandler<InviteSentEvent>> handlers = Collections.singletonList(eventHandler);
        eventListener = new InviteSentEventListener(objectMapper, handlers);
    }

    @Test
    void onMessage_ShouldCallHandlersOnValidMessage() throws IOException {
        InviteSentEvent event = new InviteSentEvent();
        when(message.getBody()).thenReturn("{\"userId\":123}".getBytes());
        when(objectMapper.readValue(any(byte[].class), eq(InviteSentEvent.class))).thenReturn(event);

        eventListener.onMessage(message, null);

        verify(eventHandler).handleEvent(event);
    }

    @Test
    void onMessage_ShouldThrowEventHandlingExceptionOnDeserializationError() throws IOException {
        when(message.getBody()).thenReturn("invalid-json".getBytes());
        when(objectMapper.readValue(any(byte[].class), eq(InviteSentEvent.class)))
                .thenThrow(new IOException("Deserialization error"));

        assertThrows(EventHandlingException.class, () -> eventListener.onMessage(message, null));
    }

    @Test
    void onMessage_ShouldWorkWithEmptyHandlersList() throws IOException {
        InviteSentEvent event = new InviteSentEvent();
        when(message.getBody()).thenReturn("{\"userId\":123}".getBytes());
        when(objectMapper.readValue(any(byte[].class), eq(InviteSentEvent.class))).thenReturn(event);

        InviteSentEventListener emptyListener = new InviteSentEventListener(objectMapper, Collections.emptyList());

        emptyListener.onMessage(message, null);

        verify(eventHandler, never()).handleEvent(any());
    }

    @Test
    void onMessage_ShouldCallAllHandlers() throws IOException {
        InviteSentEvent event = new InviteSentEvent();
        when(message.getBody()).thenReturn("{\"userId\":123}".getBytes());
        when(objectMapper.readValue(any(byte[].class), eq(InviteSentEvent.class))).thenReturn(event);

        List<EventHandler<InviteSentEvent>> handlers = List.of(handler);

        InviteSentEventListener listener = new InviteSentEventListener(objectMapper, handlers);

        listener.onMessage(message, null);

        verify(handler).handleEvent(event);
    }
}