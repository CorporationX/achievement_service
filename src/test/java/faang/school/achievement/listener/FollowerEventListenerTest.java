package faang.school.achievement.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.FollowerEvent;
import faang.school.achievement.listener.followerEvent.FollowerEventListener;
import faang.school.achievement.service.eventHandler.FollowerEventHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FollowerEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private FollowerEventHandler firstHandler, secondHandler;
    @Spy
    private ArrayList<FollowerEventHandler> followerEventHandlers;
    @Mock
    private Message message;
    private FollowerEventListener followerEventListener;
    private FollowerEvent followerEvent;
    private String messageBody, messageChannel;

    @BeforeEach
    void setUp() {
        followerEventHandlers.add(firstHandler);
        followerEventHandlers.add(secondHandler);
        followerEventListener = new FollowerEventListener(objectMapper, followerEventHandlers);

        messageBody = "\"followerId\":1,\"followeeId\":2";
        messageChannel = "follower_channel";
        followerEvent = FollowerEvent.builder()
                .followerId(1L)
                .followeeId(2L)
                .build();
    }

    @Test
    void testOnMessage() throws JsonProcessingException {
        when(message.getBody()).thenReturn(messageBody.getBytes());
        when(message.getChannel()).thenReturn(messageChannel.getBytes());
        when(objectMapper.readValue(messageBody, FollowerEvent.class)).thenReturn(followerEvent);

        followerEventListener.onMessage(message, new byte[]{});
        verify(objectMapper, times(1)).readValue(messageBody, FollowerEvent.class);
        verify(followerEventHandlers, times(1)).forEach(any());
        verify(firstHandler, times(1)).handle(followerEvent);
        verify(secondHandler, times(1)).handle(followerEvent);
    }
}