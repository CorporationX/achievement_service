package faang.school.achievement.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.ProjectEvent;
import faang.school.achievement.event.AchievementEventDispatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AchievementEventDispatcher<ProjectEvent> eventDispatcher;
    @InjectMocks
    ProjectEventListener projectEventListener;
    private Message message;
    private ProjectEvent projectEvent;

    @BeforeEach
    public void setUp() {
        projectEvent = new ProjectEvent();
        message = new Message() {
            @Override
            public byte[] getBody() {
                return new byte[0];
            }

            @Override
            public byte[] getChannel() {
                return new byte[0];
            }
        };
    }

    @Test
    void onMessageTest() throws IOException {
        when(objectMapper.readValue(message.getBody(), ProjectEvent.class)).thenReturn(projectEvent);

        projectEventListener.onMessage(message, null);

        verify(eventDispatcher, times(1)).dispatchEvent(projectEvent);
    }

    @Test
    void onMessageTest_ShouldHandleJsonProcessingException() throws IOException {
        when(objectMapper.readValue(message.getBody(), ProjectEvent.class)).thenThrow(new JsonProcessingException("Test exception") {
        });

        assertThrows(RuntimeException.class, () -> projectEventListener.onMessage(message, null));

        verify(eventDispatcher, never()).dispatchEvent(any());
    }
}
