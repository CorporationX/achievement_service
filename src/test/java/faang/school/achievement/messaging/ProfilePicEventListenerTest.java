package faang.school.achievement.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.userprofile.ProfilePicEvent;
import faang.school.achievement.handler.profilepic.ProfilePicEventHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.serializer.support.SerializationFailedException;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test cases of ProfilePicEventListenerTest")
public class ProfilePicEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ProfilePicEventHandler firstHandler;

    @Mock
    private ProfilePicEventHandler secondHandler;

    private ProfilePicEventListener listener;

    private Message message;
    private ProfilePicEvent event;

    @BeforeEach
    public void setUp() {
        setUpListener();
        setUpMessage();
        setUpEvent();
    }

    @Test
    @DisplayName("onMessage - deserialize failed")
    public void testOnMessageWithDeserializeFailed() throws IOException {
        when(objectMapper.readValue(message.getBody(), ProfilePicEvent.class))
                .thenThrow(new IOException());

        Exception exception = assertThrows(
                SerializationFailedException.class,
                () -> listener.onMessage(message, null)
        );

        assertEquals("Event deserialization failed", exception.getMessage());
    }

    @Test
    @DisplayName("onMessage - successfully")
    public void testOnMessageSuccessfully() throws IOException {
        when(objectMapper.readValue(message.getBody(), ProfilePicEvent.class)).thenReturn(event);

        listener.onMessage(message, null);

        verify(firstHandler, times(1)).handleEvent(event);
        verify(secondHandler, times(1)).handleEvent(event);
    }

    private void setUpListener() {
        listener = new ProfilePicEventListener(
                objectMapper,
                List.of(firstHandler, secondHandler)
        );
    }

    private void setUpMessage() {
        message = new Message() {
            @Override
            public byte[] getBody() {
                return "test body".getBytes();
            }

            @Override
            public byte[] getChannel() {
                return "test channel".getBytes();
            }
        };
    }

    private void setUpEvent() {
        event = ProfilePicEvent.builder()
                .userId(1L)
                .picLink("http://example.com/pic.jpg")
                .timestamp(LocalDateTime.now())
                .build();
    }
}
