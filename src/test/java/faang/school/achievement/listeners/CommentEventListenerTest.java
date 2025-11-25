package faang.school.achievement.listeners;

import faang.school.achievement.dto.CommentEventDto;
import faang.school.achievement.handlers.TimedEventHandler;
import faang.school.achievement.infrastructure.event.EventProcessingService;
import faang.school.achievement.service.DataForTestsObtainAchievements;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentEventListenerTest extends DataForTestsObtainAchievements {

    @Mock
    private EventProcessingService<CommentEventDto, TimedEventHandler<CommentEventDto>> eventProcessingService;

    @Mock
    private List<TimedEventHandler<CommentEventDto>> handlers;

    @InjectMocks
    private CommentEventListener commentEventListener;

    @Test
    void onMessage_shouldCallEventProcessingService() {
        Acknowledgment ack = mock(Acknowledgment.class);
        String expectedEventKey = String.valueOf(COMMENT_ID);
        commentEventListener.onMessage(event, ack);
        verify(eventProcessingService).process(expectedEventKey, handlers, event, ack);
    }
}