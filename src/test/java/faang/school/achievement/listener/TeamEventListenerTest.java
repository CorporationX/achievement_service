package faang.school.achievement.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.TeamEvent;
import faang.school.achievement.handler.manager.ManagerHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamEventListenerTest {
    @InjectMocks
    private TeamEventListener teamEventListener;

    @Mock
    private ManagerHandler managerHandler;

    @Spy
    private ObjectMapper objectMapper;

    @Mock
    private Message message;

    @BeforeEach
    void setUp() {
        teamEventListener = new TeamEventListener(List.of(managerHandler), objectMapper);
    }

    @Test
    void testOnMessage_whenJsonProcessingExceptionThrown() throws Exception {
        doThrow(new IOException("Test exception")).when(objectMapper)
                .readValue(any(byte[].class), Mockito.eq(TeamEvent.class));

        assertThrows(RuntimeException.class, () -> teamEventListener.onMessage(message, new byte[0]));
    }

    @Test
    void testOnMessage_successfulProcessing() throws Exception {
        TeamEvent mockEvent = new TeamEvent(12345L, 67890L, 11122L, LocalDateTime.now());
        doReturn(mockEvent).when(objectMapper).readValue(any(byte[].class), Mockito.eq(TeamEvent.class));
        String json = """
                {
                  "teamId": 12345,
                  "userId": 67890
                }""";
        when(message.getBody()).thenReturn(json.getBytes());

        teamEventListener.onMessage(message, new byte[0]);

        verify(objectMapper).readValue(any(byte[].class), Mockito.eq(TeamEvent.class));
        verify(managerHandler).startHandling(any(TeamEvent.class));
    }
}