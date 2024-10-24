package faang.school.achievement.listener.profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.achievement.profile.ProfilePicEvent;
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
class ProfilePicEventListenerTest {

    @Mock
    private Message message;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private EventHandler<ProfilePicEvent> eventHandler;

    private ProfilePicEventListener profilePicEventListener;
    private final Map<Class<?>, List<EventHandler<?>>> handlers = new HashMap<>();
    private ProfilePicEvent profilePicEvent;

    @BeforeEach
    void init() {
        profilePicEventListener = new ProfilePicEventListener(handlers, objectMapper);

        profilePicEvent = new ProfilePicEvent();
    }

    @Test
    @DisplayName("Should send event to all handlers")
    void whenListHandlersContainsMessageTypeThenSendMessageToAllHandlers() throws IOException {
        handlers.put(profilePicEvent.getClass(), List.of(eventHandler));

        when(objectMapper.readValue(message.getBody(), ProfilePicEvent.class))
                .thenReturn(profilePicEvent);

        profilePicEventListener.onMessage(message, new byte[0]);

        verify(objectMapper)
                .readValue(message.getBody(), ProfilePicEvent.class);
        verify(eventHandler)
                .handle(profilePicEvent);
    }

    @Test
    @DisplayName("When no handlers found then should throw exception")
    void whenListHandlersNotContainsMessageTypeThenThrowException() throws IOException {
        when(objectMapper.readValue(message.getBody(), ProfilePicEvent.class))
                .thenReturn(profilePicEvent);

        assertThrows(RuntimeException.class,
                () -> profilePicEventListener.onMessage(message, new byte[0]));
    }

    @Test
    @DisplayName("When object mapper can't  handle message then throw exception")
    void whenObjectMapperCantReadMessageThenThrowException() throws IOException {
        when(objectMapper.readValue(message.getBody(), ProfilePicEvent.class))
                .thenThrow(new IOException());

        assertThrows(RuntimeException.class,
                () -> profilePicEventListener.onMessage(message, new byte[0]));
    }
}