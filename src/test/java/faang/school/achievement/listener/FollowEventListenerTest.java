package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.FollowerEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.handler.FollowerEventHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FollowEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    FollowerEventHandler followerEventHandler;
    FollowEventListener followEventListener;

    @BeforeEach
    void setUp() {
        followerEventHandler = mock(FollowerEventHandler.class);
        List<EventHandler<FollowerEvent>> followerAchievementHandlers = new ArrayList<>();
        followerAchievementHandlers.add(followerEventHandler);
        followEventListener = new FollowEventListener(objectMapper, followerAchievementHandlers);
    }

    @Test
    void testOnMessage() throws IOException {
        FollowerEvent followerEvent = FollowerEvent.builder().build();
        Message message = mock(Message.class);
        byte[] pattern = "test-pattern".getBytes();
        when(objectMapper.readValue(message.getBody(), FollowerEvent.class)).thenReturn(followerEvent);
        when(followerEventHandler.supportsEvent(FollowerEvent.class)).thenReturn(true);

        followEventListener.onMessage(message, pattern);

        verify(followerEventHandler, times(1)).handleEvent(followerEvent);
    }

    @Test
    void testOnMessageThrowsExceptionTest() throws IOException {
        Message message = mock(Message.class);
        byte[] pattern = "test-pattern".getBytes();
        when(objectMapper.readValue(message.getBody(), FollowerEvent.class)).thenThrow(IOException.class);

        assertThrows(RuntimeException.class, () -> followEventListener.onMessage(message, pattern));
    }
}
