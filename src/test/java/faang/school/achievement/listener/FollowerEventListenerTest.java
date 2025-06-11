package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.FollowerEvent;
import faang.school.achievement.handler.follower.FollowerEventHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.serializer.support.SerializationFailedException;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FollowerEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private FollowerEventHandler firstHandler;

    @Mock
    private FollowerEventHandler secondHandler;

    private FollowerEventListener listener;

    private FollowerEvent event;

    private Message message;

    @BeforeEach
    void setUp() {
        listener = new FollowerEventListener(objectMapper, List.of(firstHandler, secondHandler));
        event = FollowerEvent.builder()
                .followerId(1L)
                .followeeId(2L)
                .build();
        setUpMessage();
    }

    @Test
    void shouldProcessEventSuccessfully() throws IOException {
        when(objectMapper.readValue(message.getBody(), FollowerEvent.class)).thenReturn(event);

        listener.onMessage(message, null);

        verify(firstHandler).handleEvent(event);
        verify(secondHandler).handleEvent(event);
    }

    @Test
    void shouldThrowExceptionWhenDeserializationFails() throws IOException {
        when(objectMapper.readValue(message.getBody(), FollowerEvent.class))
                .thenThrow(new IOException());

        SerializationFailedException exception = assertThrows(
                SerializationFailedException.class,
                () -> listener.onMessage(message, null)
        );

        assertEquals("Failed to deserialize FollowerEvent", exception.getMessage());
    }

    @Test
    void shouldNotFailWhenOneHandlerThrowsException() throws IOException {
        when(objectMapper.readValue(message.getBody(), FollowerEvent.class)).thenReturn(event);
        doThrow(new RuntimeException("Handler failed")).when(firstHandler).handleEvent(event);

        assertDoesNotThrow(() -> listener.onMessage(message, null));

        verify(firstHandler).handleEvent(event);
        verify(secondHandler).handleEvent(event);
    }

    private void setUpMessage() {
        message = new Message() {
            @Override
            public byte[] getBody() {
                return "test json".getBytes();
            }

            @Override
            public byte[] getChannel() {
                return "follower-events".getBytes();
            }
        };
    }
}
