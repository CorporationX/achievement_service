package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.handler.TestEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AbstractEventListenerTest {

    @Mock
    private EventHandler<TestEvent> handler;

    @Mock
    private ObjectMapper objectMapper;

    private TestEventListener testEventListener;

    @BeforeEach
    void setUp() {
        testEventListener = new TestEventListener(List.of(handler), objectMapper);
    }

    @Test
    void processEventValidTest() throws IOException {
        Message message = mock(Message.class);
        TestEvent testEvent = new TestEvent("name");
        when(message.getBody()).thenReturn("{\"name\":\"name\"}".getBytes());
        when(objectMapper.readValue(message.getBody(), TestEvent.class)).thenReturn(testEvent);

        testEventListener.processEvent(message, TestEvent.class);

        verify(handler, times(1)).handle(testEvent);
    }

    @Test
    void processEventIOExceptionTest() throws IOException {
        Message message = mock(Message.class);
        TestEvent testEvent = new TestEvent("name");
        when(message.getBody()).thenReturn("{\"name\":\"name\"}".getBytes());
        when(objectMapper.readValue(message.getBody(), TestEvent.class)).thenThrow(new IOException());

        assertThrows(RuntimeException.class, () -> testEventListener.processEvent(message, TestEvent.class));

        verify(handler, never()).handle(testEvent);
    }
}
